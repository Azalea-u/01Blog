#!/usr/bin/env bash

set -Eeuo pipefail

# -----------------------------
# Config
# -----------------------------
DB_SERVICE="db"
BACKEND_DIR="backend"
FRONTEND_DIR="frontend"
BACKEND_PORT=8080
FRONTEND_PORT=4200

# -----------------------------
# Utils
# -----------------------------
log() { echo -e "\n\033[1;34m[INFO]\033[0m $1"; }
error() { echo -e "\033[1;31m[ERROR]\033[0m $1"; }

cleanup() {
  log "Shutting down services..."
  kill 0 2>/dev/null || true
}
trap cleanup EXIT

# -----------------------------
# Validation
# -----------------------------
command -v docker >/dev/null || { error "Docker not installed"; exit 1; }
command -v docker compose >/dev/null || { error "Docker Compose not available"; exit 1; }
command -v mvn >/dev/null || command -v ./mvnw >/dev/null || { error "Maven not found"; exit 1; }
command -v npm >/dev/null || { error "npm not installed"; exit 1; }

[[ -d "$BACKEND_DIR" ]] || { error "Missing backend folder"; exit 1; }
[[ -d "$FRONTEND_DIR" ]] || { error "Missing frontend folder"; exit 1; }

# -----------------------------
# Start DB
# -----------------------------
log "Starting database..."
docker compose up -d "$DB_SERVICE"

log "Waiting for database to be healthy..."
sleep 5

# -----------------------------
# Start Backend
# -----------------------------
log "Starting backend..."
(
  cd "$BACKEND_DIR"
  if [[ -f "./mvnw" ]]; then
    ./mvnw spring-boot:run
  else
    mvn spring-boot:run
  fi
) &

BACK_PID=$!

# Wait for backend port
log "Waiting for backend on port $BACKEND_PORT..."
until nc -z localhost "$BACKEND_PORT" 2>/dev/null; do
  sleep 1
done

# -----------------------------
# Start Frontend
# -----------------------------
log "Starting frontend..."
(
  cd "$FRONTEND_DIR"
  npm install
  npm start
) &

FRONT_PID=$!

log "----------------------------------"
log "All services started successfully"
log "Backend  → http://localhost:$BACKEND_PORT"
log "Frontend → http://localhost:$FRONTEND_PORT"
log "----------------------------------"

wait

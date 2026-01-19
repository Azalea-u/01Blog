#!/bin/bash
set -e

# ------------------------------
# Color codes
# ------------------------------
RED='\033[0;31m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

BACKEND_PORT=8080
DB_PORT=5432

# ------------------------------
# Helper functions
# ------------------------------
separator() { printf "${CYAN}%$(tput cols)s${NC}\n" | tr ' ' '='; }
info() { echo -e "${CYAN}[INFO]${NC} $1"; }
success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# ------------------------------
# Check Docker Compose
# ------------------------------
info "Checking Docker Compose..."
if ! docker compose version &> /dev/null; then
    error "Docker Compose not found. Please install it."
fi
success "Docker Compose is available."
separator

# ------------------------------
# Cleanup old containers
# ------------------------------
info "Stopping and removing old containers..."
docker compose down || info "No running containers to stop."
success "Old containers cleaned."
separator

# ------------------------------
# Build and launch backend + DB
# ------------------------------
info "Building and launching backend + database..."
docker compose up --build -d
success "Containers are up and running!"
separator

# ------------------------------
# Wait a few seconds for DB
# ------------------------------
info "Waiting 5 seconds for DB initialization..."
sleep 5
success "Database should be ready."
separator

# ------------------------------
# Access info
# ------------------------------
success "01Blog backend is running!"
info "Backend: http://localhost:${BACKEND_PORT}"
info "PostgreSQL: localhost:${DB_PORT}"
separator
info "Stop all services: docker compose down"

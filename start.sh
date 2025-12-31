#!/bin/bash

echo "=============================="
echo "       Spring Boot CLI        "
echo "=============================="

read -p "Project name [demo]: " PROJECT_NAME
PROJECT_NAME=${PROJECT_NAME:-demo}

read -p "Dependencies (comma, no spaces) [web]: " DEPENDENCIES
DEPENDENCIES=${DEPENDENCIES:-web}

read -p "Java version [17]: " JAVA_VERSION
JAVA_VERSION=${JAVA_VERSION:-17}

read -p "Spring Boot version [3.5.0]: " BOOT_VERSION
BOOT_VERSION=${BOOT_VERSION:-3.5.0}

echo ""
echo "Creating project '$PROJECT_NAME' with dependencies: $DEPENDENCIES"
echo "Java version: $JAVA_VERSION, Spring Boot version: $BOOT_VERSION"
echo "=============================="
echo ""

# Create project folder
mkdir -p "$PROJECT_NAME"
cd "$PROJECT_NAME" || exit

# Build download URL
URL="https://start.spring.io/starter.zip?type=maven-project&language=java"
URL="${URL}&bootVersion=${BOOT_VERSION}&javaVersion=${JAVA_VERSION}"
URL="${URL}&dependencies=${DEPENDENCIES}"
URL="${URL}&artifactId=${PROJECT_NAME}&name=${PROJECT_NAME}&packageName=com.example.${PROJECT_NAME}"

# Download and unzip
echo "Downloading project..."
curl -L -s -o "${PROJECT_NAME}.zip" "$URL"

echo "Extracting project..."
unzip -q "${PROJECT_NAME}.zip"
rm -f "${PROJECT_NAME}.zip"

echo ""
echo "Project '$PROJECT_NAME' created successfully in folder '$PROJECT_NAME'!"
echo "Run:"
echo "  cd $PROJECT_NAME"
echo "  ./mvnw spring-boot:run"
echo "=============================="

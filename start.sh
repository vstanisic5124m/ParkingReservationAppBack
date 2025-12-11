#!/bin/bash

# Parking Reservation App - Start Script
# This script starts the backend application with default development settings

echo "=========================================="
echo "Parking Reservation App - Backend Startup"
echo "=========================================="
echo ""

# Set default environment variables if not already set
export JWT_SECRET="${JWT_SECRET:-my-super-secret-jwt-key-with-at-least-32-characters-for-security}"
export DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/auth_db}"
export DB_USERNAME="${DB_USERNAME:-postgres}"
export DB_PASSWORD="${DB_PASSWORD:-postgres}"
export SERVER_PORT="${SERVER_PORT:-8080}"
export MAIL_ENABLED="${MAIL_ENABLED:-false}"

echo "Environment Configuration:"
echo "  Database URL: $DB_URL"
echo "  Database User: $DB_USERNAME"
echo "  Server Port: $SERVER_PORT"
echo "  Mail Enabled: $MAIL_ENABLED"
echo ""

# Check if PostgreSQL is accessible
echo "Checking database connection..."
if ! pg_isready -h localhost -p 5432 -q 2>/dev/null; then
    echo "⚠️  WARNING: PostgreSQL doesn't appear to be running on localhost:5432"
    echo "   Make sure PostgreSQL is running before starting the application."
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Startup cancelled."
        exit 1
    fi
fi

# Build the application
echo "Building the application with Maven..."
echo ""
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Build failed! Please check the error messages above."
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""

# Find the JAR file
JAR_FILE=$(find target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ Could not find the JAR file in target directory"
    exit 1
fi

echo "Starting the application..."
echo "JAR file: $JAR_FILE"
echo ""
echo "=========================================="
echo "Application is starting..."
echo "Press Ctrl+C to stop the application"
echo "=========================================="
echo ""

# Start the application
java -jar "$JAR_FILE"

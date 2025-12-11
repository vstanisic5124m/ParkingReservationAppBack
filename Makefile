# Makefile for Parking Reservation App Backend
# Provides convenient commands for common tasks

.PHONY: help setup build run start test clean install

# Default target - show help
help:
	@echo "Parking Reservation App - Backend"
	@echo ""
	@echo "Available commands:"
	@echo "  make setup    - Setup the database (first time only)"
	@echo "  make install  - Install dependencies (download Maven packages)"
	@echo "  make build    - Build the application"
	@echo "  make run      - Build and run the application"
	@echo "  make start    - Quick start (same as 'make run')"
	@echo "  make test     - Run tests"
	@echo "  make clean    - Clean build artifacts"
	@echo ""
	@echo "Quick start:"
	@echo "  1. make setup   (first time only)"
	@echo "  2. make start"

# Setup database
setup:
	@echo "Setting up database..."
	@./setup-db.sh

# Install dependencies
install:
	@echo "Installing dependencies..."
	@mvn dependency:resolve

# Build the application
build:
	@echo "Building application..."
	@mvn clean package -DskipTests

# Run the application (build first)
run: build
	@echo "Starting application..."
	@java -jar target/auth-service-0.0.1-SNAPSHOT.jar

# Alias for run
start: run

# Run tests
test:
	@echo "Running tests..."
	@mvn test

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@mvn clean
	@echo "Clean complete!"

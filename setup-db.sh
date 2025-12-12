#!/bin/bash

# Database Setup Script for Parking Reservation App
# This script creates the PostgreSQL database required by the application

echo "=========================================="
echo "Database Setup for Parking Reservation App"
echo "=========================================="
echo ""

DB_NAME="${DB_NAME:-auth_db}"
DB_USER="${DB_USERNAME:-vukstanisic}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "Database Configuration:"
echo "  Database Name: $DB_NAME"
echo "  Database User: $DB_USER"
echo "  Database Host: $DB_HOST"
echo "  Database Port: $DB_PORT"
echo ""

# Check if PostgreSQL is running
if ! pg_isready -h "$DB_HOST" -p "$DB_PORT" -q 2>/dev/null; then
    echo "❌ PostgreSQL is not running on $DB_HOST:$DB_PORT"
    echo ""
    echo "Please start PostgreSQL before running this script."
    echo "On Ubuntu/Debian: sudo systemctl start postgresql"
    echo "On macOS with Homebrew: brew services start postgresql"
    exit 1
fi

echo "✅ PostgreSQL is running"
echo ""

# Check if database already exists
if psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw "$DB_NAME"; then
    echo "⚠️  Database '$DB_NAME' already exists!"
    echo ""
    read -p "Do you want to drop and recreate it? (y/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Dropping existing database..."
        dropdb -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME"
        if [ $? -ne 0 ]; then
            echo "❌ Failed to drop database. You may need to run: sudo -u postgres dropdb $DB_NAME"
            exit 1
        fi
        echo "✅ Database dropped"
    else
        echo "Keeping existing database. Setup complete!"
        exit 0
    fi
fi

# Create the database
echo "Creating database '$DB_NAME'..."
createdb -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME"

if [ $? -ne 0 ]; then
    echo "❌ Failed to create database"
    echo ""
    echo "If you don't have permission, try running:"
    echo "  sudo -u postgres createdb $DB_NAME"
    echo ""
    echo "Or connect to PostgreSQL and run:"
    echo "  psql -U postgres"
    echo "  CREATE DATABASE $DB_NAME;"
    exit 1
fi

echo "✅ Database '$DB_NAME' created successfully!"
echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "The database schema will be automatically created by Liquibase"
echo "when you start the application for the first time."
echo ""
echo "To start the application, run:"
echo "  ./start.sh"

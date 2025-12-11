# Getting Started - Parking Reservation App Backend

This document provides a complete guide to set up and run the Parking Reservation App backend service.

## Table of Contents
- [System Requirements](#system-requirements)
- [Quick Start Guide](#quick-start-guide)
- [Detailed Setup Instructions](#detailed-setup-instructions)
- [Configuration Options](#configuration-options)
- [Running the Application](#running-the-application)
- [Troubleshooting](#troubleshooting)

## System Requirements

### Required Software
- **Java**: Version 17 or higher
  - Check: `java -version`
  - Download: https://adoptium.net/
  
- **Maven**: Version 3.6 or higher
  - Check: `mvn -version`
  - Download: https://maven.apache.org/download.cgi
  
- **PostgreSQL**: Version 12 or higher
  - Check: `psql --version`
  - Download: https://www.postgresql.org/download/

### System Resources
- At least 2GB of free RAM
- At least 500MB of disk space

## Quick Start Guide

### For Linux/macOS Users

1. **Setup Database** (first time only):
   ```bash
   ./setup-db.sh
   ```

2. **Start the Application**:
   ```bash
   ./start.sh
   ```

That's it! The application will be available at `http://localhost:8080`

### For Windows Users

1. **Setup Database** (first time only):
   - Open PostgreSQL command line or pgAdmin
   - Run: `CREATE DATABASE auth_db;`

2. **Start the Application**:
   ```cmd
   start.bat
   ```

The application will be available at `http://localhost:8080`

## Detailed Setup Instructions

### Step 1: Install Prerequisites

#### Installing Java (if not installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**macOS (using Homebrew):**
```bash
brew install openjdk@17
```

**Windows:**
- Download from https://adoptium.net/
- Run the installer and follow the instructions
- Make sure to add Java to your PATH

#### Installing Maven (if not installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install maven
```

**macOS (using Homebrew):**
```bash
brew install maven
```

**Windows:**
- Download from https://maven.apache.org/download.cgi
- Extract to a directory (e.g., C:\Program Files\Maven)
- Add Maven's bin directory to your PATH

#### Installing PostgreSQL (if not installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

**macOS (using Homebrew):**
```bash
brew install postgresql
brew services start postgresql
```

**Windows:**
- Download from https://www.postgresql.org/download/windows/
- Run the installer
- Remember the password you set for the 'postgres' user

### Step 2: Clone the Repository

```bash
git clone https://github.com/vstanisic5124m/ParkingReservationAppBack.git
cd ParkingReservationAppBack
```

### Step 3: Configure the Database

#### Option A: Using the setup script (Linux/macOS only)

```bash
chmod +x setup-db.sh
./setup-db.sh
```

#### Option B: Manual setup

**Linux/macOS:**
```bash
# Connect to PostgreSQL (you may need to use: sudo -u postgres psql)
psql -U postgres

# In the PostgreSQL prompt:
CREATE DATABASE auth_db;
\q
```

**Windows:**
```cmd
# Open Command Prompt or PowerShell
# Connect to PostgreSQL
psql -U postgres

# In the PostgreSQL prompt:
CREATE DATABASE auth_db;
\q
```

### Step 4: Configure Environment Variables (Optional)

The application works with default values for development. If you need to customize:

#### Create a `.env` file (optional)

Copy the example file:
```bash
cp .env.example .env
```

Edit `.env` to match your configuration:
```bash
JWT_SECRET=your-custom-secret-key-min-32-chars
DB_URL=jdbc:postgresql://localhost:5432/auth_db
DB_USERNAME=postgres
DB_PASSWORD=your-postgres-password
```

#### Export environment variables

**Linux/macOS:**
```bash
source .env  # If you created a .env file
# Or export manually:
export JWT_SECRET="your-custom-secret-key-min-32-chars"
export DB_PASSWORD="your-postgres-password"
```

**Windows (Command Prompt):**
```cmd
set JWT_SECRET=your-custom-secret-key-min-32-chars
set DB_PASSWORD=your-postgres-password
```

**Windows (PowerShell):**
```powershell
$env:JWT_SECRET="your-custom-secret-key-min-32-chars"
$env:DB_PASSWORD="your-postgres-password"
```

## Configuration Options

The application can be configured using environment variables. Here are all available options:

| Variable | Default | Required | Description |
|----------|---------|----------|-------------|
| `JWT_SECRET` | Auto-generated | Yes* | Secret key for JWT token signing (min 32 chars) |
| `DB_URL` | `jdbc:postgresql://localhost:5432/auth_db` | Yes | Database connection URL |
| `DB_USERNAME` | `postgres` | Yes | Database username |
| `DB_PASSWORD` | `postgres` | Yes | Database password |
| `SERVER_PORT` | `8080` | No | Port where the server will listen |
| `MAIL_ENABLED` | `false` | No | Enable email functionality |
| `MAIL_HOST` | `smtp.gmail.com` | No | SMTP server host |
| `MAIL_PORT` | `587` | No | SMTP server port |
| `MAIL_USERNAME` | - | No | Email username |
| `MAIL_PASSWORD` | - | No | Email password |
| `LOG_LEVEL` | `INFO` | No | Application log level |
| `SHOW_SQL` | `false` | No | Show SQL queries in logs |

\* The start scripts provide a default value for development

## Running the Application

### Using the Start Scripts (Recommended)

**Linux/macOS:**
```bash
./start.sh
```

**Windows:**
```cmd
start.bat
```

The scripts will:
1. Set default environment variables
2. Build the application
3. Start the server

### Manual Start

If you prefer to start manually:

```bash
# Build the application
mvn clean package -DskipTests

# Run the JAR file
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Verifying the Application is Running

1. Look for this message in the console:
   ```
   Started AuthServiceApplication in X.XXX seconds
   ```

2. Test the API:
   ```bash
   curl http://localhost:8080/api/auth/login
   ```

## Troubleshooting

### Common Issues and Solutions

#### 1. "JAVA_HOME not set" error

**Solution:**
Set the JAVA_HOME environment variable to your JDK installation directory.

**Linux/macOS:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64  # Adjust path as needed
```

**Windows:**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17  # Adjust path as needed
```

#### 2. "Connection to localhost:5432 refused"

**Cause:** PostgreSQL is not running or not accepting connections.

**Solution:**
- Check if PostgreSQL is running:
  ```bash
  # Linux/macOS
  pg_isready
  
  # Or check the service
  sudo systemctl status postgresql  # Linux
  brew services list                # macOS
  ```

- Start PostgreSQL if it's not running:
  ```bash
  sudo systemctl start postgresql   # Linux
  brew services start postgresql    # macOS
  ```

- On Windows, check Services app for "postgresql" service

#### 3. "Database auth_db does not exist"

**Solution:**
Run the database setup script or create the database manually:
```bash
./setup-db.sh
# Or manually:
psql -U postgres -c "CREATE DATABASE auth_db;"
```

#### 4. "Port 8080 already in use"

**Solution:**
Either stop the other application using port 8080, or run on a different port:
```bash
export SERVER_PORT=8081
./start.sh
```

#### 5. Build fails with Maven errors

**Solution:**
- Clear Maven cache and rebuild:
  ```bash
  mvn clean
  rm -rf ~/.m2/repository  # Use with caution
  mvn package
  ```

#### 6. "Cannot find symbol" compilation errors

**Solution:**
This was an issue with incomplete code that has been fixed. Pull the latest changes:
```bash
git pull origin main
```

### Getting Help

If you encounter an issue not covered here:

1. Check the application logs for detailed error messages
2. Verify all prerequisites are correctly installed
3. Ensure your PostgreSQL credentials are correct
4. Check that ports 8080 and 5432 are not blocked by firewall
5. Open an issue on GitHub with:
   - The error message
   - Your operating system
   - Java, Maven, and PostgreSQL versions

## Next Steps

Once the application is running:

1. Test the API endpoints (see README.md)
2. Set up the frontend application
3. Explore the API documentation
4. Configure production settings for deployment

## Additional Resources

- [Main README](README.md) - Overview and API testing examples
- [Quick Start Guide](Quick_Start.md) - Frontend integration instructions
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

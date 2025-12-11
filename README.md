# Parking Reservation App - Backend

Authentication and authorization service for the Parking Reservation System.

## 🚀 Quick Start

### Prerequisites

Before starting, ensure you have:

- ✅ Java 17 or higher (`java -version`)
- ✅ Maven 3.6+ (`mvn -version`)
- ✅ PostgreSQL 12+ installed and running

### Option 1: Automated Setup (Recommended)

**Linux/Mac:**
```bash
# 1. Setup the database (first time only)
./setup-db.sh

# 2. Start the application
./start.sh
```

**Windows:**
```cmd
REM 1. Create the database manually (see Database Setup section below)

REM 2. Start the application
start.bat
```

The application will start on `http://localhost:8080` 🎉

### Option 2: Manual Setup

#### Step 1: Clone the Repository

```bash
git clone https://github.com/vstanisic5124m/ParkingReservationAppBack.git
cd ParkingReservationAppBack
```

#### Step 2: Database Setup

**Option A: Using the setup script (Linux/Mac only):**
```bash
./setup-db.sh
```

**Option B: Manual setup:**
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE auth_db;

# Exit PostgreSQL
\q
```

#### Step 3: Configure Environment Variables (Optional)

The application comes with sensible defaults for development. If you need to customize:

**Linux/Mac:**
```bash
export JWT_SECRET="your-custom-secret-key-min-32-chars"
export DB_URL="jdbc:postgresql://localhost:5432/auth_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="your-postgres-password"
```

**Windows (Command Prompt):**
```cmd
set JWT_SECRET=your-custom-secret-key-min-32-chars
set DB_URL=jdbc:postgresql://localhost:5432/auth_db
set DB_USERNAME=postgres
set DB_PASSWORD=your-postgres-password
```

**Windows (PowerShell):**
```powershell
$env:JWT_SECRET="your-custom-secret-key-min-32-chars"
$env:DB_URL="jdbc:postgresql://localhost:5432/auth_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-postgres-password"
```

#### Step 4: Build and Run

```bash
# Build the application
mvn clean package

# Run the application
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

The server will start on port 8080:
```
Started AuthServiceApplication in X.XXX seconds
```

## 🧪 Testing the API

You can test the backend API directly using curl:

### Register a user:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 📁 Project Structure

```
ParkingReservationAppBack/
├── src/
│   ├── main/
│   │   ├── java/com/parkingshare/auth/    # Java source code
│   │   └── resources/
│   │       ├── application.yml             # Application configuration
│   │       └── db/changelog/               # Liquibase database migrations
│   └── test/                               # Test files
├── .env.example                            # Environment variables template
├── .gitignore                              # Git ignore file
├── setup-db.sh                             # Database setup script (Linux/Mac)
├── start.sh                                # Application start script (Linux/Mac)
├── start.bat                               # Application start script (Windows)
├── pom.xml                                 # Maven configuration
└── README.md                               # This file
```

## ⚙️ Configuration

All configuration is in `src/main/resources/application.yml`. The application uses environment variables for sensitive data:

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | *(see .env.example)* | JWT signing key (min 32 characters) |
| `DB_URL` | `jdbc:postgresql://localhost:5432/auth_db` | Database connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `SERVER_PORT` | `8080` | Server port |
| `MAIL_ENABLED` | `false` | Enable/disable email features |

See `.env.example` for a complete list of available configuration options.

## ⚠️ Troubleshooting

### Backend Issues

**Problem: "JWT_SECRET is required"**
- Solution: The start scripts set this automatically. If running manually, export the JWT_SECRET environment variable.

**Problem: "Unable to connect to database"**
- Solution: 
  - Verify PostgreSQL is running: `pg_isready`
  - Check if the database exists: `psql -U postgres -l | grep auth_db`
  - Run the setup script: `./setup-db.sh`

**Problem: "Port 8080 already in use"**
- Solution: Set a different port: `export SERVER_PORT=8081` (Linux/Mac) or `set SERVER_PORT=8081` (Windows)

**Problem: Build fails with "JAVA_HOME not set"**
- Solution: Set JAVA_HOME to your JDK installation directory

**Problem: Maven command not found**
- Solution: Install Maven or use the Maven wrapper if available

### Database Issues

**Problem: "relation does not exist" errors**
- Solution: Liquibase will automatically create tables on first run. Ensure the application starts successfully at least once.

**Problem: Permission denied when creating database**
- Solution: Run `sudo -u postgres createdb auth_db` or create it via psql as a superuser

## 🛑 Stopping the Application

Press `Ctrl+C` in the terminal where the application is running.

## 🔧 Development

### Running Tests
```bash
mvn test
```

### Building without running tests
```bash
mvn clean package -DskipTests
```

### Running with custom profile
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## 📝 License

This project is part of the Parking Reservation System.

## 🔗 Related Repositories

- Frontend: [ParkingReservationAppFront](https://github.com/vstanisic5124m/ParkingReservationAppFront)

## 📞 Support

If you encounter any issues not covered in this README, please open an issue on GitHub.

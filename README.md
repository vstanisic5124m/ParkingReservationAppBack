# ParkingReservationAppBack

## Autogenereted


## Prerequisites Check

Before starting, ensure you have:

- ✅ Java 17 or higher (`java -version`)
- ✅ Maven 3.6+ (`mvn -version`)
- ✅ PostgreSQL 12+ installed and running
- ✅ Node.js 18+ (`node -version`)
- ✅ npm (`npm -version`)

## Step-by-Step Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/vstanisic5124m/ParkingReservartion.git
cd ParkingReservartion
```

### Step 2: Database Setup

Create the PostgreSQL database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE auth_db;

# Exit PostgreSQL
\q
```

### Step 3: Configure Backend Environment Variables

Set the required environment variables:

**Linux/Mac:**
```bash
export JWT_SECRET="my-super-secret-jwt-key-with-at-least-32-characters-for-security"
export DB_URL="jdbc:postgresql://localhost:5432/auth_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="your-postgres-password"
```

**Windows (Command Prompt):**
```cmd
set JWT_SECRET=my-super-secret-jwt-key-with-at-least-32-characters-for-security
set DB_URL=jdbc:postgresql://localhost:5432/auth_db
set DB_USERNAME=postgres
set DB_PASSWORD=your-postgres-password
```

**Windows (PowerShell):**
```powershell
$env:JWT_SECRET="my-super-secret-jwt-key-with-at-least-32-characters-for-security"
$env:DB_URL="jdbc:postgresql://localhost:5432/auth_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-postgres-password"
```

### Step 4: Start the Backend

```bash
# Build the backend
mvn clean package

# Run the backend
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

You should see output indicating the server started on port 8080:
```
Started AuthServiceApplication in X.XXX seconds
```

**Keep this terminal running!**

### Step 5: Start the Frontend

Open a **new terminal** window:

```bash
# Navigate to the frontend directory
cd frontend

# Install dependencies (first time only)
npm install

# Start the development server
npm start
```

You should see:
```
** Angular Live Development Server is listening on localhost:4200 **
```

**Keep this terminal running too!**

### Step 6: Access the Application

Open your web browser and navigate to:

```
http://localhost:4200
```

## Testing the Application

### 1. Register a New User

1. Click on "Register here" link or navigate to `http://localhost:4200/register`
2. Fill in the registration form:
   - **Email**: test@example.com
   - **Password**: password123 (minimum 8 characters)
   - **First Name**: John
   - **Last Name**: Doe
   - **Phone Number**: +1234567890 (optional)
3. Click "Register"
4. You should be automatically logged in and redirected to the dashboard

### 2. Test Login

1. Click "Logout" from the dashboard
2. You'll be redirected to the login page
3. Enter your credentials:
   - **Email**: test@example.com
   - **Password**: password123
4. Click "Login"
5. You should be redirected to the dashboard

### 3. Test Protected Routes

1. While logged out, try to access `http://localhost:4200/dashboard`
2. You should be redirected to the login page
3. After logging in, you can access the dashboard

## Troubleshooting

### Backend Issues

**Problem: "JWT_SECRET is required"**
- Solution: Make sure you've set the JWT_SECRET environment variable

**Problem: "Unable to connect to database"**
- Solution: Verify PostgreSQL is running and the database exists
- Check your DB_URL, DB_USERNAME, and DB_PASSWORD are correct

**Problem: "Port 8080 already in use"**
- Solution: Stop any other application using port 8080 or set SERVER_PORT environment variable to a different port

### Frontend Issues

**Problem: "CORS Error" in browser console**
- Solution: Make sure the backend is running and CORS is properly configured

**Problem: "Cannot connect to backend"**
- Solution: Verify the backend is running on http://localhost:8080
- Check the API URL in `frontend/src/app/services/auth.service.ts`

**Problem: "Port 4200 already in use"**
- Solution: Run `ng serve --port 4300` to use a different port

**Problem: npm install fails**
- Solution: Delete `node_modules` and `package-lock.json`, then run `npm install` again

## API Testing with curl

You can also test the backend API directly:

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

## Stopping the Application

### To stop the backend:
- Press `Ctrl+C` in the backend terminal

### To stop the frontend:
- Press `Ctrl+C` in the frontend terminal

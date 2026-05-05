# Online Quiz System

A Java-based application for conducting online quizzes with secure login, quiz management, and instant result evaluation.

## Features

- User registration and login
- Admin panel for quiz and user management
- Quiz creation with multiple-choice questions
- Real-time quiz taking and evaluation
- Result tracking and performance analysis

## Technology Stack

- **Backend**: Java
- **Frontend**: Java Swing
- **Database**: H2 Database
- **Connectivity**: JDBC

## Prerequisites

- Java 11 or higher
- Maven (for building)

## Setup and Running

1. **Clone or download the project**

2. **Build the project** (optional, as classes are pre-compiled):
   ```
   mvn clean compile
   ```

3. **Run the application**:
   - **GUI Mode** (default): `java -cp <classpath> com.onlinequizsystem.Main`
   - **Command-Line Testing**:
     - Test login: `java -cp <classpath> com.onlinequizsystem.Main test-login`
     - Test registration: `java -cp <classpath> com.onlinequizsystem.Main test-register`
     - List quizzes: `java -cp <classpath> com.onlinequizsystem.Main list-quizzes`
     - Test quiz taking: `java -cp <classpath> com.onlinequizsystem.Main test-quiz`

   Replace `<classpath>` with the appropriate classpath including H2 JAR and compiled classes.

## Default Credentials

- **Admin**: Username: `admin`, Password: `admin123`

## Database

The application uses H2 embedded database. The database file is created automatically in the project directory (`./online_quiz_system.mv.db`). Sample data is inserted on first run.

## Usage

1. Run the application in GUI mode.
2. Login as admin or register a new user.
3. Admins can create quizzes and manage users.
4. Users can take available quizzes and view results.

- Java 11 or higher
- MySQL Server
- Maven

## Setup Instructions

1. **Clone or download the project**

2. **Set up MySQL Database**
   - Create a database named `online_quiz_system`
   - Run the `database_schema.sql` script to create tables and insert sample data

3. **Configure Database Connection**
   - Update `src/main/resources/db.properties` with your MySQL credentials:
     ```
     db.url=jdbc:mysql://localhost:3306/online_quiz_system?useSSL=false&serverTimezone=UTC
     db.username=your_username
     db.password=your_password
     ```

4. **Build the Project**
   - Open terminal in project root
   - Run: `mvn clean compile`

5. **Run the Application**
   - Run: `mvn exec:java -Dexec.mainClass="com.onlinequizsystem.Main"`
   - Or run Main.java directly if using IDE

## Usage

- **Login**: Use admin/admin123 for admin access or register as a new user
- **Admin**: Create quizzes, add questions, manage users, view results
- **User**: Take available quizzes, view personal results

## Project Structure

```
src/main/java/com/onlinequizsystem/
├── Main.java                 # Application entry point
├── model/                    # Data models
│   ├── User.java
│   ├── Quiz.java
│   ├── Question.java
│   └── Result.java
├── dao/                      # Data access objects
│   ├── UserDAO.java
│   ├── QuizDAO.java
│   ├── QuestionDAO.java
│   └── ResultDAO.java
├── controller/               # Business logic
│   ├── UserController.java
│   └── QuizController.java
├── util/                     # Utilities
│   └── DBConnection.java
└── view/                     # GUI components
    ├── LoginView.java
    ├── RegisterView.java
    ├── AdminDashboardView.java
    ├── UserDashboardView.java
    └── QuizTakingView.java
```

## Database Schema

- `users`: User accounts
- `quizzes`: Quiz information
- `questions`: Quiz questions with options
- `results`: Quiz attempt results

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is for educational purposes.
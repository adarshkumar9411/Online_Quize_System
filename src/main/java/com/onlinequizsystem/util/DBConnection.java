package com.onlinequizsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties props = new Properties();
                InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
                if (input == null) {
                    throw new RuntimeException("db.properties not found in classpath");
                }
                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(url, username, password);

                // Initialize database schema
                initializeSchema();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
        return connection;
    }

    private static void initializeSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create tables
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "role VARCHAR(10) NOT NULL CHECK (role IN ('admin', 'user'))" +
                ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS quizzes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "description TEXT, " +
                "created_by INT NOT NULL, " +
                "FOREIGN KEY (created_by) REFERENCES users(id)" +
                ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "quiz_id INT NOT NULL, " +
                "question_text TEXT NOT NULL, " +
                "options VARCHAR(1000) NOT NULL, " +
                "correct_answer VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE" +
                ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS results (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "quiz_id INT NOT NULL, " +
                "score INT NOT NULL, " +
                "total_questions INT NOT NULL, " +
                "date_taken TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
                ")");

            // Insert sample admin user if not exists
            stmt.execute("INSERT INTO users (username, password, email, role) " +
                "SELECT 'admin', 'admin123', 'admin@example.com', 'admin' " +
                "WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')");

            // Insert sample quiz if not exists
            stmt.execute("INSERT INTO quizzes (title, description, created_by) " +
                "SELECT 'Sample Quiz', 'A sample quiz to test the system', 1 " +
                "WHERE NOT EXISTS (SELECT 1 FROM quizzes WHERE title = 'Sample Quiz')");

            // Insert sample questions if not exists
            stmt.execute("INSERT INTO questions (quiz_id, question_text, options, correct_answer) " +
                "SELECT 1, 'What is 2 + 2?', '3,4,5,6', '4' " +
                "WHERE NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'What is 2 + 2?')");

            stmt.execute("INSERT INTO questions (quiz_id, question_text, options, correct_answer) " +
                "SELECT 1, 'What is the capital of France?', 'London,Paris,Berlin,Madrid', 'Paris' " +
                "WHERE NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'What is the capital of France?')");

            stmt.execute("INSERT INTO questions (quiz_id, question_text, options, correct_answer) " +
                "SELECT 1, 'Which planet is known as the Red Planet?', 'Venus,Earth,Mars,Jupiter', 'Mars' " +
                "WHERE NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which planet is known as the Red Planet?')");
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
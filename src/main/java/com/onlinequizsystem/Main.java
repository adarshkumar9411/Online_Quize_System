package com.onlinequizsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import com.onlinequizsystem.controller.QuizController;
import com.onlinequizsystem.controller.UserController;
import com.onlinequizsystem.model.Question;
import com.onlinequizsystem.model.Quiz;
import com.onlinequizsystem.model.User;
import com.onlinequizsystem.view.LoginView;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            runCommandLine(args);
        } else {
            SwingUtilities.invokeLater(() -> {
                new LoginView().setVisible(true);
            });
        }
    }

    private static void runCommandLine(String[] args) {
        String command = args[0];
        switch (command) {
            case "test-login":
                testLogin();
                break;
            case "test-register":
                testRegister();
                break;
            case "test-quiz":
                testQuiz();
                break;
            case "list-quizzes":
                listQuizzes();
                break;
            default:
                System.out.println("Available commands: test-login, test-register, test-quiz, list-quizzes");
        }
    }

    private static void testLogin() {
        UserController userController = new UserController();
        System.out.println("Testing login with admin/admin123");
        User user = userController.loginUser("admin", "admin123");
        if (user != null) {
            System.out.println("Login successful: " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("Login failed");
        }
    }

    private static void testRegister() {
        UserController userController = new UserController();
        System.out.println("Testing registration of testuser");
        User newUser = new User(0, "testuser", "password123", "test@example.com", "user");
        boolean success = userController.registerUser(newUser);
        if (success) {
            System.out.println("Registration successful");
        } else {
            System.out.println("Registration failed (username may exist)");
        }
    }

    private static void listQuizzes() {
        QuizController quizController = new QuizController();
        System.out.println("Available quizzes:");
        List<Quiz> quizzes = quizController.getAllQuizzes();
        for (Quiz q : quizzes) {
            System.out.println("- " + q.getTitle() + " (ID: " + q.getId() + ")");
        }
    }

    private static void testQuiz() {
        QuizController quizController = new QuizController();
        UserController userController = new UserController();
        System.out.println("Testing quiz taking");
        User user = userController.loginUser("admin", "admin123");
        if (user == null) {
            System.out.println("Cannot login admin for testing");
            return;
        }
        List<Quiz> quizzes = quizController.getAllQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available");
            return;
        }
        Quiz quiz = quizController.getQuizById(quizzes.get(0).getId());
        if (quiz == null || quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            System.out.println("Quiz has no questions");
            return;
        }
        System.out.println("Taking quiz: " + quiz.getTitle());
        Map<Integer, String> answers = new HashMap<>();
        for (Question q : quiz.getQuestions()) {
            // Simulate answering with the first option
            answers.put(q.getId(), q.getOptions().get(0));
            System.out.println("Answered: " + q.getQuestionText() + " -> " + q.getOptions().get(0));
        }
        int score = quizController.evaluateQuiz(user.getId(), quiz.getId(), answers);
        System.out.println("Quiz completed. Score: " + score + "/" + quiz.getQuestions().size());
    }
}
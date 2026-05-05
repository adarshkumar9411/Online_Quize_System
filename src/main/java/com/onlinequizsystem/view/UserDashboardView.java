package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.QuizController;
import com.onlinequizsystem.model.Quiz;
import com.onlinequizsystem.model.Result;
import com.onlinequizsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserDashboardView extends JFrame {
    private User user;
    private QuizController quizController;
    private JList<Quiz> quizList;
    private DefaultListModel<Quiz> quizModel;
    private JList<Result> resultList;
    private DefaultListModel<Result> resultModel;

    public UserDashboardView(User user) {
        this.user = user;
        quizController = new QuizController();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("User Dashboard - " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Quizzes Tab
        JPanel quizPanel = new JPanel(new BorderLayout());
        quizModel = new DefaultListModel<>();
        quizList = new JList<>(quizModel);
        quizPanel.add(new JScrollPane(quizList), BorderLayout.CENTER);

        JButton takeQuizBtn = new JButton("Take Quiz");
        takeQuizBtn.addActionListener(e -> takeSelectedQuiz());
        quizPanel.add(takeQuizBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("Available Quizzes", quizPanel);

        // Results Tab
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultModel = new DefaultListModel<>();
        resultList = new JList<>(resultModel);
        resultPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        tabbedPane.addTab("My Results", resultPanel);

        // Logout button
        JPanel bottomPanel = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        bottomPanel.add(logoutBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        // Load quizzes
        List<Quiz> quizzes = quizController.getAllQuizzes();
        System.out.println("Debug: Loaded " + quizzes.size() + " quizzes");
        quizModel.clear();
        for (Quiz q : quizzes) {
            quizModel.addElement(q);
            System.out.println("Debug: Quiz: " + q.getTitle());
        }

        // Load user results
        List<Result> results = quizController.getUserResults(user.getId());
        System.out.println("Debug: Loaded " + results.size() + " results for user " + user.getId());
        resultModel.clear();
        for (Result r : results) {
            resultModel.addElement(r);
        }
    }

    private void takeSelectedQuiz() {
        Quiz selected = quizList.getSelectedValue();
        System.out.println("Debug: takeSelectedQuiz called, selected quiz: " + (selected != null ? selected.getTitle() : "null"));
        if (selected != null) {
            Quiz fullQuiz = quizController.getQuizById(selected.getId());
            System.out.println("Debug: Full quiz loaded with " + (fullQuiz.getQuestions() != null ? fullQuiz.getQuestions().size() : 0) + " questions");
            new QuizTakingView(user, fullQuiz).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a quiz to take.");
        }
    }

    private void logout() {
        dispose();
        new LoginView().setVisible(true);
    }
}
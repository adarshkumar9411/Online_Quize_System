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
        quizModel.clear();
        for (Quiz q : quizzes) {
            quizModel.addElement(q);
        }

        // Load user results
        List<Result> results = quizController.getUserResults(user.getId());
        resultModel.clear();
        for (Result r : results) {
            resultModel.addElement(r);
        }
    }

    private void takeSelectedQuiz() {
        Quiz selected = quizList.getSelectedValue();
        if (selected != null) {
            new QuizTakingView(user, selected).setVisible(true);
        }
    }

    private void logout() {
        dispose();
        new LoginView().setVisible(true);
    }
}
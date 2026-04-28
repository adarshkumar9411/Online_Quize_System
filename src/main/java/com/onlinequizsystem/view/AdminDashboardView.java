package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.QuizController;
import com.onlinequizsystem.controller.UserController;
import com.onlinequizsystem.model.Quiz;
import com.onlinequizsystem.model.Result;
import com.onlinequizsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private User admin;
    private QuizController quizController;
    private UserController userController;
    private JList<Quiz> quizList;
    private DefaultListModel<Quiz> quizModel;
    private JList<User> userList;
    private DefaultListModel<User> userModel;
    private JList<Result> resultList;
    private DefaultListModel<Result> resultModel;

    public AdminDashboardView(User admin) {
        this.admin = admin;
        quizController = new QuizController();
        userController = new UserController();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Quizzes Tab
        JPanel quizPanel = new JPanel(new BorderLayout());
        quizModel = new DefaultListModel<>();
        quizList = new JList<>(quizModel);
        quizPanel.add(new JScrollPane(quizList), BorderLayout.CENTER);

        JPanel quizButtons = new JPanel();
        JButton createQuizBtn = new JButton("Create Quiz");
        createQuizBtn.addActionListener(e -> new CreateQuizView(this, admin.getId()).setVisible(true));
        JButton editQuizBtn = new JButton("Edit Quiz");
        editQuizBtn.addActionListener(e -> editSelectedQuiz());
        JButton deleteQuizBtn = new JButton("Delete Quiz");
        deleteQuizBtn.addActionListener(e -> deleteSelectedQuiz());
        quizButtons.add(createQuizBtn);
        quizButtons.add(editQuizBtn);
        quizButtons.add(deleteQuizBtn);
        quizPanel.add(quizButtons, BorderLayout.SOUTH);

        tabbedPane.addTab("Quizzes", quizPanel);

        // Users Tab
        JPanel userPanel = new JPanel(new BorderLayout());
        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel userButtons = new JPanel();
        JButton deleteUserBtn = new JButton("Delete User");
        deleteUserBtn.addActionListener(e -> deleteSelectedUser());
        userButtons.add(deleteUserBtn);
        userPanel.add(userButtons, BorderLayout.SOUTH);

        tabbedPane.addTab("Users", userPanel);

        // Results Tab
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultModel = new DefaultListModel<>();
        resultList = new JList<>(resultModel);
        resultPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        tabbedPane.addTab("Results", resultPanel);

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

    void loadData() {
        // Load quizzes
        List<Quiz> quizzes = quizController.getAllQuizzes();
        quizModel.clear();
        for (Quiz q : quizzes) {
            quizModel.addElement(q);
        }

        // Load users
        List<User> users = userController.getAllUsers();
        userModel.clear();
        for (User u : users) {
            userModel.addElement(u);
        }

        // Load results
        List<Result> results = quizController.getAllResults();
        resultModel.clear();
        for (Result r : results) {
            resultModel.addElement(r);
        }
    }

    private void editSelectedQuiz() {
        Quiz selected = quizList.getSelectedValue();
        if (selected != null) {
            new EditQuizView(this, selected).setVisible(true);
        }
    }

    private void deleteSelectedQuiz() {
        Quiz selected = quizList.getSelectedValue();
        if (selected != null && JOptionPane.showConfirmDialog(this, "Delete quiz?") == JOptionPane.YES_OPTION) {
            if (quizController.deleteQuiz(selected.getId())) {
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete quiz!");
            }
        }
    }

    private void deleteSelectedUser() {
        User selected = userList.getSelectedValue();
        if (selected != null && JOptionPane.showConfirmDialog(this, "Delete user?") == JOptionPane.YES_OPTION) {
            userController.deleteUser(selected.getId());
            loadData();
        }
    }

    private void logout() {
        dispose();
        new LoginView().setVisible(true);
    }
}
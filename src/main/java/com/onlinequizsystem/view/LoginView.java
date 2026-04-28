package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.UserController;
import com.onlinequizsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserController userController;

    public LoginView() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Quiz System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> new RegisterView().setVisible(true));
        panel.add(registerButton);

        add(panel);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = userController.loginUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(LoginView.this, "Login successful!");
                dispose();
                if ("admin".equals(user.getRole())) {
                    new AdminDashboardView(user).setVisible(true);
                } else {
                    new UserDashboardView(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(LoginView.this, "Invalid credentials!");
            }
        }
    }
}
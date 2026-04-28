package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.UserController;
import com.onlinequizsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox<String> roleCombo;
    private JButton registerButton;
    private UserController userController;

    public RegisterView() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Quiz System - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        panel.add(roleCombo);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());
        panel.add(registerButton);

        add(panel);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String role = (String) roleCombo.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterView.this, "All fields are required!");
                return;
            }

            User user = new User(0, username, password, email, role);
            if (userController.registerUser(user)) {
                JOptionPane.showMessageDialog(RegisterView.this, "Registration successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(RegisterView.this, "Registration failed! Username may be taken.");
            }
        }
    }
}
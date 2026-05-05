package com.onlinequizsystem.controller;

import com.onlinequizsystem.dao.UserDAO;
import com.onlinequizsystem.model.User;
import java.util.List;

public class UserController {
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(User user) {
        System.out.println("Debug: Attempting to register user: " + user.getUsername());
        // Check if username exists
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            System.out.println("Debug: Username already exists: " + user.getUsername());
            return false; // Username taken
        }
        boolean result = userDAO.registerUser(user);
        System.out.println("Debug: Registration result for " + user.getUsername() + ": " + result);
        return result;
    }

    public User loginUser(String username, String password) {
        System.out.println("Debug: Attempting login for user: " + username);
        User user = userDAO.loginUser(username, password);
        System.out.println("Debug: Login result for " + username + ": " + (user != null ? "success" : "failed"));
        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
}
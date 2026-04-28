package com.onlinequizsystem.controller;

import com.onlinequizsystem.dao.UserDAO;
import com.onlinequizsystem.model.User;
import java.util.List;

public class UserController {
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(User user) {
        // Check if username exists
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            return false; // Username taken
        }
        return userDAO.registerUser(user);
    }

    public User loginUser(String username, String password) {
        return userDAO.loginUser(username, password);
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
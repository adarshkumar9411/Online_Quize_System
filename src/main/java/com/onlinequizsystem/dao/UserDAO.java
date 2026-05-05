package com.onlinequizsystem.dao;

import com.onlinequizsystem.model.User;
import com.onlinequizsystem.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(User user) {
        System.out.println("Debug: DAO registerUser called for: " + user.getUsername());
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            int rows = stmt.executeUpdate();
            System.out.println("Debug: Inserted " + rows + " rows for user: " + user.getUsername());
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Debug: SQLException in registerUser: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"),
                                    rs.getString("password"), rs.getString("email"), rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User loginUser(String username, String password) {
        System.out.println("Debug: DAO loginUser called for: " + username);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getInt("id"), rs.getString("username"),
                                    rs.getString("password"), rs.getString("email"), rs.getString("role"));
                    System.out.println("Debug: Found user: " + user.getUsername() + ", role: " + user.getRole());
                    return user;
                } else {
                    System.out.println("Debug: No user found for: " + username);
                }
            }
        } catch (SQLException e) {
            System.out.println("Debug: SQLException in loginUser: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"),
                                   rs.getString("password"), rs.getString("email"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
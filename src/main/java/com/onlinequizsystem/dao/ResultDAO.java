package com.onlinequizsystem.dao;

import com.onlinequizsystem.model.Result;
import com.onlinequizsystem.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    public boolean saveResult(Result result) {
        String sql = "INSERT INTO results (user_id, quiz_id, score, total_questions, date_taken) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, result.getUserId());
            stmt.setInt(2, result.getQuizId());
            stmt.setInt(3, result.getScore());
            stmt.setInt(4, result.getTotalQuestions());
            stmt.setTimestamp(5, result.getDateTaken());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Result> getResultsByUser(int userId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE user_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(new Result(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("quiz_id"),
                                       rs.getInt("score"), rs.getInt("total_questions"), rs.getTimestamp("date_taken")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Result> getResultsByQuiz(int quizId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE quiz_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(new Result(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("quiz_id"),
                                       rs.getInt("score"), rs.getInt("total_questions"), rs.getTimestamp("date_taken")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(new Result(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("quiz_id"),
                                       rs.getInt("score"), rs.getInt("total_questions"), rs.getTimestamp("date_taken")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
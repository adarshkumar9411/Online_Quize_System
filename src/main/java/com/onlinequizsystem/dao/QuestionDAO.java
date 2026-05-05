package com.onlinequizsystem.dao;

import com.onlinequizsystem.model.Question;
import com.onlinequizsystem.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionDAO {

    public boolean addQuestion(Question question) {
        String sql = "INSERT INTO questions (quiz_id, question_text, options, correct_answer) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, String.join(",", question.getOptions()));
            stmt.setString(4, question.getCorrectAnswer());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Question> getQuestionsByQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> options = Arrays.asList(rs.getString("options").split(","));
                    questions.add(new Question(rs.getInt("id"), rs.getInt("quiz_id"),
                                              rs.getString("question_text"), options,
                                              rs.getString("correct_answer")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public boolean updateQuestion(Question question) {
        String sql = "UPDATE questions SET question_text = ?, options = ?, correct_answer = ? WHERE id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, String.join(",", question.getOptions()));
            stmt.setString(3, question.getCorrectAnswer());
            stmt.setInt(4, question.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteQuestion(int id) {
        String sql = "DELETE FROM questions WHERE id = ?";
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
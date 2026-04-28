package com.onlinequizsystem.model;

import java.sql.Timestamp;

public class Result {
    private int id;
    private int userId;
    private int quizId;
    private int score;
    private int totalQuestions;
    private Timestamp dateTaken;

    public Result() {}

    public Result(int id, int userId, int quizId, int score, int totalQuestions, Timestamp dateTaken) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.dateTaken = dateTaken;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public Timestamp getDateTaken() { return dateTaken; }
    public void setDateTaken(Timestamp dateTaken) { this.dateTaken = dateTaken; }

    @Override
    public String toString() {
        return "Score: " + score + "/" + totalQuestions + " on " + dateTaken;
    }
}
package com.onlinequizsystem.controller;

import com.onlinequizsystem.dao.QuizDAO;
import com.onlinequizsystem.dao.QuestionDAO;
import com.onlinequizsystem.dao.ResultDAO;
import com.onlinequizsystem.model.Quiz;
import com.onlinequizsystem.model.Question;
import com.onlinequizsystem.model.Result;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class QuizController {
    private QuizDAO quizDAO = new QuizDAO();
    private QuestionDAO questionDAO = new QuestionDAO();
    private ResultDAO resultDAO = new ResultDAO();

    public boolean createQuiz(Quiz quiz) {
        int quizId = quizDAO.createQuiz(quiz);
        if (quizId > 0 && quiz.getQuestions() != null) {
            for (Question q : quiz.getQuestions()) {
                q.setQuizId(quizId);
                questionDAO.addQuestion(q);
            }
            return true;
        }
        return quizId > 0;
    }

    public List<Quiz> getAllQuizzes() {
        return quizDAO.getAllQuizzes();
    }

    public Quiz getQuizById(int id) {
        Quiz quiz = quizDAO.getQuizById(id);
        if (quiz != null) {
            quiz.setQuestions(questionDAO.getQuestionsByQuiz(id));
        }
        return quiz;
    }

    public boolean addQuestionToQuiz(Question question) {
        return questionDAO.addQuestion(question);
    }

    public int evaluateQuiz(int userId, int quizId, Map<Integer, String> answers) {
        List<Question> questions = questionDAO.getQuestionsByQuiz(quizId);
        int score = 0;
        for (Question q : questions) {
            String userAnswer = answers.get(q.getId());
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                score++;
            }
        }
        // Save result
        Result result = new Result(0, userId, quizId, score, questions.size(), new Timestamp(System.currentTimeMillis()));
        resultDAO.saveResult(result);
        return score;
    }

    public List<Result> getUserResults(int userId) {
        return resultDAO.getResultsByUser(userId);
    }

    public List<Result> getAllResults() {
        return resultDAO.getAllResults();
    }

    public boolean updateQuiz(Quiz quiz) {
        return quizDAO.updateQuiz(quiz);
    }

    public boolean deleteQuiz(int id) {
        return quizDAO.deleteQuiz(id);
    }

    public List<Question> getQuestionsByQuiz(int quizId) {
        return questionDAO.getQuestionsByQuiz(quizId);
    }

    public boolean addQuestion(Question question) {
        return questionDAO.addQuestion(question);
    }

    public boolean updateQuestion(Question question) {
        return questionDAO.updateQuestion(question);
    }

    public boolean deleteQuestion(int id) {
        return questionDAO.deleteQuestion(id);
    }
}
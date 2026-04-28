package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.QuizController;
import com.onlinequizsystem.model.Question;
import com.onlinequizsystem.model.Quiz;
import com.onlinequizsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizTakingView extends JFrame {
    private User user;
    private Quiz quiz;
    private QuizController quizController;
    private List<Question> questions;
    private Map<Integer, String> answers;
    private int currentQuestionIndex;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JButton nextButton;
    private JButton submitButton;

    public QuizTakingView(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        quizController = new QuizController();
        questions = quiz.getQuestions();
        answers = new HashMap<>();
        currentQuestionIndex = 0;
        initializeUI();
        showQuestion();
    }

    private void initializeUI() {
        setTitle("Taking Quiz: " + quiz.getTitle());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionButtons = new JRadioButton[4];
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            group.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        panel.add(optionsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextAction());
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitAction());
        submitButton.setEnabled(false);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText((currentQuestionIndex + 1) + ". " + q.getQuestionText());
            List<String> options = q.getOptions();
            for (int i = 0; i < options.size(); i++) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setSelected(false);
            }
            if (currentQuestionIndex == questions.size() - 1) {
                nextButton.setEnabled(false);
                submitButton.setEnabled(true);
            }
        }
    }

    private class NextAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Save answer
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    answers.put(questions.get(currentQuestionIndex).getId(), optionButtons[i].getText());
                    break;
                }
            }
            currentQuestionIndex++;
            showQuestion();
        }
    }

    private class SubmitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Save last answer
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    answers.put(questions.get(currentQuestionIndex).getId(), optionButtons[i].getText());
                    break;
                }
            }
            // Evaluate
            int score = quizController.evaluateQuiz(user.getId(), quiz.getId(), answers);
            JOptionPane.showMessageDialog(QuizTakingView.this, "Quiz completed! Score: " + score + "/" + questions.size());
            dispose();
        }
    }
}
package com.onlinequizsystem.view;

import com.onlinequizsystem.controller.QuizController;
import com.onlinequizsystem.model.Question;
import com.onlinequizsystem.model.Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreateQuizView extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton addQuestionBtn;
    private JButton saveBtn;
    private JButton cancelBtn;
    private List<Question> questions;
    private QuizController quizController;
    private int adminId;
    private AdminDashboardView parent;

    public CreateQuizView(AdminDashboardView parent, int adminId) {
        this.parent = parent;
        this.adminId = adminId;
        this.questions = new ArrayList<>();
        this.quizController = new QuizController();

        setTitle("Create New Quiz");
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        titleField = new JTextField(30);
        descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);

        addQuestionBtn = new JButton("Add Question");
        addQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        saveBtn = new JButton("Save Quiz");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuiz();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(addQuestionBtn, gbc);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addQuestion() {
        JTextField questionField = new JTextField(20);
        JTextField option1Field = new JTextField(15);
        JTextField option2Field = new JTextField(15);
        JTextField option3Field = new JTextField(15);
        JTextField option4Field = new JTextField(15);
        JComboBox<String> correctAnswerBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Question:"));
        panel.add(questionField);
        panel.add(new JLabel("Option 1:"));
        panel.add(option1Field);
        panel.add(new JLabel("Option 2:"));
        panel.add(option2Field);
        panel.add(new JLabel("Option 3:"));
        panel.add(option3Field);
        panel.add(new JLabel("Option 4:"));
        panel.add(option4Field);
        panel.add(new JLabel("Correct Answer:"));
        panel.add(correctAnswerBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Question", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String questionText = questionField.getText().trim();
            String opt1 = option1Field.getText().trim();
            String opt2 = option2Field.getText().trim();
            String opt3 = option3Field.getText().trim();
            String opt4 = option4Field.getText().trim();
            String correct = (String) correctAnswerBox.getSelectedItem();

            if (!questionText.isEmpty() && !opt1.isEmpty() && !opt2.isEmpty() && !opt3.isEmpty() && !opt4.isEmpty()) {
                List<String> options = new ArrayList<>();
                options.add(opt1);
                options.add(opt2);
                options.add(opt3);
                options.add(opt4);

                String correctAnswer = "";
                switch (correct) {
                    case "Option 1": correctAnswer = opt1; break;
                    case "Option 2": correctAnswer = opt2; break;
                    case "Option 3": correctAnswer = opt3; break;
                    case "Option 4": correctAnswer = opt4; break;
                }

                Question question = new Question(0, 0, questionText, options, correctAnswer);
                questions.add(question);
                JOptionPane.showMessageDialog(this, "Question added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        }
    }

    private void saveQuiz() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill title and description!");
            return;
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question!");
            return;
        }

        Quiz quiz = new Quiz(0, title, description, adminId);
        quiz.setQuestions(questions);

        if (quizController.createQuiz(quiz)) {
            JOptionPane.showMessageDialog(this, "Quiz created successfully!");
            parent.loadData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create quiz!");
        }
    }
}
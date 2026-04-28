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

public class EditQuizView extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JList<Question> questionList;
    private DefaultListModel<Question> questionModel;
    private JButton addQuestionBtn;
    private JButton editQuestionBtn;
    private JButton deleteQuestionBtn;
    private JButton saveBtn;
    private JButton cancelBtn;
    private Quiz quiz;
    private QuizController quizController;
    private AdminDashboardView parent;

    public EditQuizView(AdminDashboardView parent, Quiz quiz) {
        this.parent = parent;
        this.quiz = quiz;
        this.quizController = new QuizController();

        setTitle("Edit Quiz");
        setModal(true);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        titleField = new JTextField(quiz.getTitle(), 30);
        descriptionArea = new JTextArea(quiz.getDescription(), 3, 30);
        descriptionArea.setLineWrap(true);

        questionModel = new DefaultListModel<>();
        questionList = new JList<>(questionModel);

        addQuestionBtn = new JButton("Add Question");
        addQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        editQuestionBtn = new JButton("Edit Question");
        editQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedQuestion();
            }
        });

        deleteQuestionBtn = new JButton("Delete Question");
        deleteQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedQuestion();
            }
        });

        saveBtn = new JButton("Save Changes");
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

        add(inputPanel, BorderLayout.NORTH);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(new JScrollPane(questionList), BorderLayout.CENTER);

        JPanel questionButtons = new JPanel();
        questionButtons.add(addQuestionBtn);
        questionButtons.add(editQuestionBtn);
        questionButtons.add(deleteQuestionBtn);
        questionPanel.add(questionButtons, BorderLayout.SOUTH);

        add(questionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        List<Question> questions = quizController.getQuestionsByQuiz(quiz.getId());
        questionModel.clear();
        for (Question q : questions) {
            questionModel.addElement(q);
        }
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

                Question question = new Question(0, quiz.getId(), questionText, options, correctAnswer);
                if (quizController.addQuestion(question)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Question added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add question!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        }
    }

    private void editSelectedQuestion() {
        Question selected = questionList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a question to edit!");
            return;
        }

        JTextField questionField = new JTextField(selected.getQuestionText(), 20);
        List<String> options = selected.getOptions();
        JTextField option1Field = new JTextField(options.get(0), 15);
        JTextField option2Field = new JTextField(options.get(1), 15);
        JTextField option3Field = new JTextField(options.get(2), 15);
        JTextField option4Field = new JTextField(options.get(3), 15);
        JComboBox<String> correctAnswerBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});
        String correctAnswer = selected.getCorrectAnswer();
        if (correctAnswer.equals(options.get(0))) correctAnswerBox.setSelectedItem("Option 1");
        else if (correctAnswer.equals(options.get(1))) correctAnswerBox.setSelectedItem("Option 2");
        else if (correctAnswer.equals(options.get(2))) correctAnswerBox.setSelectedItem("Option 3");
        else if (correctAnswer.equals(options.get(3))) correctAnswerBox.setSelectedItem("Option 4");

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

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Question", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String questionText = questionField.getText().trim();
            String opt1 = option1Field.getText().trim();
            String opt2 = option2Field.getText().trim();
            String opt3 = option3Field.getText().trim();
            String opt4 = option4Field.getText().trim();
            String correct = (String) correctAnswerBox.getSelectedItem();

            if (!questionText.isEmpty() && !opt1.isEmpty() && !opt2.isEmpty() && !opt3.isEmpty() && !opt4.isEmpty()) {
                List<String> newOptions = new ArrayList<>();
                newOptions.add(opt1);
                newOptions.add(opt2);
                newOptions.add(opt3);
                newOptions.add(opt4);

                String newCorrectAnswer = "";
                switch (correct) {
                    case "Option 1": newCorrectAnswer = opt1; break;
                    case "Option 2": newCorrectAnswer = opt2; break;
                    case "Option 3": newCorrectAnswer = opt3; break;
                    case "Option 4": newCorrectAnswer = opt4; break;
                }

                selected.setQuestionText(questionText);
                selected.setOptions(newOptions);
                selected.setCorrectAnswer(newCorrectAnswer);

                if (quizController.updateQuestion(selected)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Question updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update question!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        }
    }

    private void deleteSelectedQuestion() {
        Question selected = questionList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a question to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (quizController.deleteQuestion(selected.getId())) {
                loadData();
                JOptionPane.showMessageDialog(this, "Question deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete question!");
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

        quiz.setTitle(title);
        quiz.setDescription(description);

        if (quizController.updateQuiz(quiz)) {
            JOptionPane.showMessageDialog(this, "Quiz updated successfully!");
            parent.loadData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update quiz!");
        }
    }
}
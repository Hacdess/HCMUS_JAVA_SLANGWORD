package src.view;

import src.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class QuizGameFrame extends JFrame {

    private final SlangWordDictionary dict;
    private int mode = 1; // 1: Slang → Definition, 2: Definition → Slang

    // Centralized fonts – easy to change
    private static final Font TITLE_FONT       = new Font("Segoe UI", Font.BOLD, 34);
    private static final Font BUTTON_FONT      = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font QUESTION_FONT   = new Font("Segoe UI", Font.BOLD, 23);
    private static final Font OPTION_FONT      = new Font("Segoe UI", Font.PLAIN, 20);
    private static final Font CONTROL_FONT    = new Font("Segoe UI", Font.BOLD, 18);

    public QuizGameFrame(JFrame parent, SlangWordDictionary dict) {
        super("Quiz Game - Lê Trung Kiên 23127075");
        this.dict = dict;
        setSize(780, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        showModeSelection();
    }

    // ------------------- MODE SELECTION SCREEN -------------------
    private void showModeSelection() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridLayout(4, 1, 25, 25));
        panel.setBorder(new EmptyBorder(60, 140, 60, 140));

        JLabel title = new JLabel("SELECT QUIZ MODE", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);

        JButton mode1 = new JButton("1. Slang Word → Definition");
        mode1.setFont(BUTTON_FONT);
        mode1.addActionListener(e -> { mode = 1; startQuiz(); });

        JButton mode2 = new JButton("2. Definition → Slang Word");
        mode2.setFont(BUTTON_FONT);
        mode2.addActionListener(e -> { mode = 2; startQuiz(); });

        JButton back = new JButton("Back to Main Menu");
        back.setFont(BUTTON_FONT);
        back.addActionListener(e -> dispose());

        panel.add(title);
        panel.add(mode1);
        panel.add(mode2);
        panel.add(back);

        setContentPane(panel);
        revalidate();
        repaint();
    }

    // ------------------- QUIZ PLAY SCREEN -------------------
    private void startQuiz() {
        getContentPane().removeAll();

        Quiz<String, List<String>> q = dict.creatQuestion();
        if (q == null) {
            JOptionPane.showMessageDialog(this, "Not enough data to start quiz!");
            showModeSelection();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Question text
        String question = mode == 1
                ? "What is the meaning of \"" + q.getCorrect().getKey() + "\"?"
                : "Which slang word means \"" + q.getCorrect().getValue().get(0) + "\"?";

        JLabel qLabel = new JLabel(question, SwingConstants.CENTER);
        qLabel.setFont(QUESTION_FONT);

        // Options
        List<String> options = new java.util.ArrayList<>();
        if (mode == 1) {
            options.add(q.getCorrect().getValue().get(0));
            q.getIncorrect().forEach(e -> options.add(e.getValue().get(0)));
        } else {
            options.add(q.getCorrect().getKey());
            q.getIncorrect().forEach(e -> options.add(e.getKey()));
        }
        Collections.shuffle(options);

        JPanel optPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        ButtonGroup group = new ButtonGroup();
        String correctAnswer = mode == 1 ? q.getCorrect().getValue().get(0) : q.getCorrect().getKey();

        for (String opt : options) {
            JRadioButton rb = new JRadioButton(opt);
            rb.setFont(OPTION_FONT);
            group.add(rb);
            optPanel.add(rb);
        }

        // Control buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JButton checkBtn = new JButton("Check Answer");
        checkBtn.setFont(CONTROL_FONT);
        checkBtn.addActionListener(e -> {
            String selected = "";
            for (Component c : optPanel.getComponents()) {
                if (c instanceof JRadioButton rb && rb.isSelected()) {
                    selected = rb.getText();
                    break;
                }
            }

            String msg = selected.equals(correctAnswer)
                    ? "CORRECT! Well done!"
                    : "WRONG! The correct answer is: " + correctAnswer;

            JOptionPane.showMessageDialog(this, msg, "Result", JOptionPane.INFORMATION_MESSAGE);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Play another question?", "Continue?",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                startQuiz();    // New random question
            } else {
                dispose();      // Close quiz
            }
        });

        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(CONTROL_FONT);
        backBtn.addActionListener(e -> dispose());

        btnPanel.add(checkBtn);
        btnPanel.add(backBtn);

        mainPanel.add(qLabel, BorderLayout.NORTH);
        mainPanel.add(optPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }
}
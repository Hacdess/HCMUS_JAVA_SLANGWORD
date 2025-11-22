package src.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

import src.SlangWordDictionary;

public class AppFrame extends JFrame{
    private final String FONT = "Segoe UI";
    private final SlangWordDictionary dictionary;
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JRadioButton rbBySlang, rbByDef;
    private String currentSelectedSlang = null;

    public AppFrame(SlangWordDictionary dictionary) {
        this.dictionary = dictionary;

        setTitle("SLANG WORD DICTIONARY");
        
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEAD =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 45, 90));
        headerPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        // === Title ===
        JLabel titleLabel = new JLabel("SLANG WORD DICTIONARY", SwingConstants.CENTER);
        titleLabel.setFont(new Font(FONT, Font.BOLD, 42));
        titleLabel.setForeground(Color.CYAN);

        // === Student info ===
        JLabel nameLabel = new JLabel("Fullname: Lê Trung Kiên", SwingConstants.CENTER);
        nameLabel.setFont(new Font(FONT, Font.PLAIN, 32));
        nameLabel.setForeground(Color.WHITE);

        JLabel idLabel = new JLabel("Student ID: 23127075", SwingConstants.CENTER);
        idLabel.setFont(new Font(FONT, Font.PLAIN, 32));
        idLabel.setForeground(Color.WHITE);

        // === Wrap up title and Student info ===
        JPanel titleBox = new JPanel(new GridLayout(3, 1, 5, 5));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(nameLabel);
        titleBox.add(idLabel);

        headerPanel.add(titleBox, BorderLayout.NORTH);

        // ====== Search bar =====
        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(new CompoundBorder(
            new LineBorder(new Color(150, 150, 150), 2),
            new EmptyBorder(14, 25, 14, 25)
        ));    

        searchField = new JTextField();
        searchField.setFont(new Font(FONT, Font.PLAIN, 28));

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font(FONT, Font.BOLD, 28));
        searchBtn.setBackground(new Color(60, 120, 220));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(140, 54));
        searchBtn.addActionListener(e -> performSearch());

        rbBySlang = new JRadioButton("By slang", true);
        rbBySlang.setFont(new Font(FONT, Font.BOLD, 28));
        rbBySlang.setPreferredSize(new Dimension(140, 50));
        rbByDef = new JRadioButton("By definition");
        rbByDef.setFont(new Font(FONT, Font.BOLD, 28));
        ButtonGroup btGroup = new ButtonGroup();
        btGroup.add(rbBySlang);
        btGroup.add(rbByDef);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        radioPanel.setOpaque(false);
        radioPanel.add(rbBySlang);
        radioPanel.add(rbByDef);

        searchBox.add(searchField, BorderLayout.CENTER);
        searchBox.add(searchBtn, BorderLayout.EAST);

        JPanel topSearch = new JPanel(new BorderLayout());
        topSearch.setOpaque(false);
        topSearch.add(searchBox, BorderLayout.CENTER);
        topSearch.add(radioPanel, BorderLayout.EAST);

        headerPanel.add(topSearch, BorderLayout.SOUTH);

        // ===== 6 function buttons ======
        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        btnPanel.setPreferredSize(new Dimension(900, 400));
        btnPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        btnPanel.setOpaque(false);

        JPanel btnPanelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanelWrapper.add(btnPanel);
        btnPanelWrapper.setPreferredSize(new Dimension(900, 400));


        String[] btnTexts = {"Add slang", "Edit slang", "Delete slang", "History", "Reset dict", "Random slang"};
        for (String text : btnTexts) {
            JButton btn = new JButton(text);
            btn.setFont(new Font(FONT, Font.BOLD, 28));
            btn.setBackground(new Color(190, 190, 190));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> handleButton(text));
            btnPanel.add(btn);
        }

        // ===== Result area =====
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 25));
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(30, 30, 30, 30));
        resultArea.setText("Welcome to Slang Word Dictionary!\n\nEnter a word and press Search to start...");
        resultArea.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(new LineBorder(new Color(180, 180, 180), 2));

        // ===== Play Game =====
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(25, 60, 40, 60));
        bottom.setOpaque(false);

        JTextField placeholder = new JTextField("Type your answer during quiz...");
        placeholder.setFont(new Font(FONT, Font.ITALIC, 25));
        placeholder.setForeground(Color.GRAY);
        placeholder.setEnabled(false);

        JButton playBtn = new JButton("Play Game");
        playBtn.setFont(new Font(FONT, Font.BOLD, 28));
        playBtn.setBackground(new Color(255, 70, 70));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFocusPainted(false);
        playBtn.setPreferredSize(new Dimension(280, 70));
        playBtn.addActionListener(e -> new QuizGameFrame(this, dictionary).setVisible(true)); // MỞ CỬA SỔ MỚI

        bottom.add(placeholder, BorderLayout.CENTER);
        bottom.add(playBtn, BorderLayout.EAST);

        // ===== JOIN ALL =====
        add(headerPanel, BorderLayout.NORTH);
        add(btnPanelWrapper, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JLabel label = new JLabel("Enter the word to find!");
            label.setFont(new Font(FONT, Font.PLAIN, 28));
            JOptionPane.showMessageDialog(this, label, "Title", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        resultArea.setText("");
        if (rbBySlang.isSelected()) {
            List<String> meanings = dictionary.findBySlang(keyword);
            if (meanings != null) {
                resultArea.append(keyword + " → " + String.join(" | ", meanings));
            } else {
                resultArea.append("Can't find: " + keyword);
            }
        } else {
            List<String> slangs = dictionary.findByDefinition(keyword);
            if (slangs != null && !slangs.isEmpty()) {
                resultArea.append("Find " + slangs.size() + " slangs containing \"" + keyword + "\":\n");
                resultArea.append(String.join(", ", slangs));
            } else {
                resultArea.append("Can't find any slang containing: " + keyword);
            }
        }
    }

    private void handleButton(String text) {
        switch (text) {
            case "Add slang" -> showAddDialog();
            case "Edit slang" -> showEditDialog();
            case "Delete slang" -> showDeleteDialog();
            case "History" -> showHistory();
            case "Reset dict" -> resetDictionary();
            case "Random slang" -> showRandomSlang();
        }
    }

    private void showAddDialog() {
        JTextField slangField = new JTextField(20);
        JTextField meaningField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Slang word:"));
        panel.add(slangField);
        panel.add(new JLabel("Meanings (comma separated):"));
        panel.add(meaningField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Slang", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String slang = slangField.getText().trim();
            String[] meaningsArr = meaningField.getText().split(",");
            java.util.List<String> meanings = java.util.Arrays.stream(meaningsArr)
                    .map(String::trim).filter(s -> !s.isEmpty()).toList();

            if (!slang.isEmpty() && !meanings.isEmpty()) {
                dictionary.addSlangWord(slang, meanings, null);
                JOptionPane.showMessageDialog(this, "Added successfully!");
            }
        }
    }

    private void showEditDialog() {
        String slang = JOptionPane.showInputDialog(this, "Enter slang to edit:");
        if (slang != null && !slang.trim().isEmpty()) {
            String newMeanings = JOptionPane.showInputDialog(this, "New meanings (comma separated):");
            if (newMeanings != null) {
                java.util.List<String> meanings = java.util.Arrays.stream(newMeanings.split(","))
                        .map(String::trim).filter(s -> !s.isEmpty()).toList();
                dictionary.getData().put(slang.trim(), meanings);
                dictionary.exportFile();
                dictionary.useBuildKeyWordIndex();
                JOptionPane.showMessageDialog(this, "Edited successfully!");
            }
        }
    }

    private void showDeleteDialog() {
        String slang = JOptionPane.showInputDialog(this, "Enter slang to delete:");
        if (slang != null && !slang.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete \"" + slang + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dictionary.deleteSlangWord(slang.trim());
                JOptionPane.showMessageDialog(this, "Deleted successfully!");
            }
        }
    }

    private void showHistory() {
        resultArea.setText("=== SEARCHING HISTORY ===\n");
        if (dictionary.getHistory().isEmpty()) {
            resultArea.append("History is empty.\n");
        } else {
            dictionary.getHistory().forEach(h -> resultArea.append("• " + h + "\n"));
        }
    }

    private void showRandomSlang() {
        resultArea.setText("SLANG OF THE DAY\n\n" + dictionary.randomSlang());
    }

    private void resetDictionary() {
        int c = JOptionPane.showConfirmDialog(this, "Confirm to reset the dictionary?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            dictionary.resetDictionary();
            JOptionPane.showMessageDialog(this, "Resest dictionary successfully!");
        }
    }
}

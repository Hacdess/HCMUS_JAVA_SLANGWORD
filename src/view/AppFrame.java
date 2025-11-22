package src.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

import src.SlangWordDictionary;

public class AppFrame extends JFrame{
    private final SlangWordDictionary dictionary;
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JRadioButton rbBySlang, rbByDef;

    public AppFrame(SlangWordDictionary dictionary) {
        this.dictionary = dictionary;

        setTitle("SLANG WORD DICTIONARY");
        
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
}

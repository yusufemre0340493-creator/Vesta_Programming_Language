package com.mycompany.vestaide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VestaIDE extends JFrame {

    private final JTextArea codeArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final Interpreter interpreter = new Interpreter();

    public VestaIDE() {
        setTitle("Vesta IDE");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === TOP BAR ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(25, 25, 25));

        JButton runButton = new JButton("▶ Run");
        runButton.setBackground(new Color(45, 45, 45));
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);

        topBar.add(runButton);

        // === CODE AREA ===
        codeArea.setBackground(new Color(30, 30, 30));
        codeArea.setForeground(new Color(220, 220, 220));
        codeArea.setCaretColor(Color.WHITE);
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        codeArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // === OUTPUT AREA ===
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(new Color(0, 255, 120));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        enableAutoComplete(codeArea);

        JScrollPane codeScroll = new JScrollPane(codeArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        codeScroll.setBorder(null);
        outputScroll.setBorder(null);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                codeScroll,
                outputScroll);

        splitPane.setDividerLocation(380);
        splitPane.setDividerSize(4);
        splitPane.setBackground(new Color(25, 25, 25));

        runButton.addActionListener(e -> runCode());

        add(topBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(25, 25, 25));
    }

    private void runCode() {
        outputArea.setText("");

        interpreter.setOutput(text -> SwingUtilities.invokeLater(() -> outputArea.append(text + "\n")));

        String[] lines = codeArea.getText().split("\n");

        try {
            for (String line : lines) {
                interpreter.execute(line.trim());
            }
        } catch (Exception e) {
            outputArea.append("Hata: " + e.getMessage());
        }
    }

    private void enableAutoComplete(JTextArea area) {
        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (c == '"' || c == '(') {
                    SwingUtilities.invokeLater(() -> {
                        if (c == '"') {
                            area.insert("\"", area.getCaretPosition());
                        } else if (c == '(') {
                            area.insert(")", area.getCaretPosition());
                        }
                    });
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VestaIDE().setVisible(true));
    }
}

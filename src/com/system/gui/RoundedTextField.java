package com.system.gui;

import javax.swing.*;
import java.awt.*;

public class RoundedTextField extends JTextField {

    private int cornerRadius = 12;

    public RoundedTextField() {
        super();
        init();
    }

    public RoundedTextField(int columns) {
        super(columns);
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g);
        g2.dispose();
    }
}
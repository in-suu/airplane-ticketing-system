package com.system.gui;

import javax.swing.*;
import java.awt.*;

public class RoundedTextField extends JTextField {

    private int cornerRadius = 5;

    public RoundedTextField(int columns) {
        super(columns);

        setOpaque(false);

        setBorder(null);
        setUI(new javax.swing.plaf.basic.BasicTextFieldUI());

        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🔥 SET THICKNESS HERE
        g2.setStroke(new BasicStroke(1)); // change 2 → any value

        g2.setColor(new Color(18, 81, 123));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
}
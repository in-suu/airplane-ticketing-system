package com.system.gui;

import javax.swing.*;
import java.awt.*;

public class ActionButton extends JButton {
    private static final long serialVersionUID = 1L;
    private Color bgColor;

    /** No-arg constructor for WindowBuilder compatibility */
    public ActionButton() {
        this("Button", Color.WHITE, Color.BLACK);
    }

    public ActionButton(String text, Color bg, Color fg) {
        super(text);
        this.bgColor = bg;
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(fg);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override 
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}

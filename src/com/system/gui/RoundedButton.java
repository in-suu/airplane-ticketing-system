package com.system.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {

    private static final long serialVersionUID = 1L;
    private int radius = 20;
    private Color defaultColor = new Color(255, 255, 255, 230);
    private Color hoverColor = Color.WHITE;
    private Color pressedColor = new Color(240, 240, 240);
    private boolean isHovered = false;

    /** Default constructor for WindowBuilder compatibility */
    public RoundedButton() {
        this("Button");
    }

    public RoundedButton(String text) {
        super(text);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setBackground(defaultColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        setBackground(defaultColor);
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = radius > 0 ? radius : h;

        boolean isPressed = getModel().isPressed();

        // Shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(2, 2, w - 4, h - 4, arc, arc);

        if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(defaultColor);
        }
        g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);

        g2.setColor(new Color(200, 200, 200, 50));
        g2.drawRoundRect(0, 0, w - 3, h - 3, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
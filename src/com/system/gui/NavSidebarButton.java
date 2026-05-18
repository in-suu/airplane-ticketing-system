package com.system.gui;

import javax.swing.*;
import java.awt.*;
import com.system.models.DataManager;

public class NavSidebarButton extends JButton {
    private static final long serialVersionUID = 1L;
    private final Color REFINED_NAVY = new Color(30, 45, 75);
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private boolean isActive = false;

    /** Default constructor for WindowBuilder compatibility */
    public NavSidebarButton() {
        this("Button");
    }

    public NavSidebarButton(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        updateTextColor();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                updateTextColor();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                updateTextColor();
            }
        });
    }

    public void setActive(boolean active) {
        this.isActive = active;
        updateTextColor();
        repaint();
    }

    private void updateTextColor() {
        if (getText().equals("EXIT")) {
            setForeground(Color.WHITE);
        } else if (isActive || getModel().isRollover()) {
            setForeground(DataManager.SUNSET_ORANGE);
        } else {
            setForeground(new Color(50, 50, 50));
        }
    }

    @Override 
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        String text = getText();
        
        if (text.equals("EXIT")) {
            g2.setColor(getModel().isRollover() ? SOFT_CARD_NAVY : REFINED_NAVY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
        } else if (isActive || getModel().isRollover()) {
            // Dark translucent fill over the bright sidebar
            g2.setColor(new Color(40, 20, 10, 160)); 
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            // Bright orange left accent border
            g2.setColor(DataManager.SUNSET_ORANGE);
            g2.fillRoundRect(0, 0, 5, getHeight(), 5, 5);
        } else {
            // Normal Inactive state
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}

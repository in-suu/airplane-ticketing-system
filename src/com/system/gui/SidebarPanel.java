package com.system.gui;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image bgImage;

    public SidebarPanel() {
        // Default constructor for WindowBuilder
        setLayout(null);
    }

    public void setBackgroundImagePath(String path) {
        try { 
            bgImage = new ImageIcon(getClass().getResource(path)).getImage(); 
            repaint();
        } catch (Exception e) {
            System.err.println("Could not load image: " + path);
        }
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 120, 0, 150), 0, getHeight(), new Color(200, 60, 0, 180)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Fallback for WindowBuilder design mode if image isn't loaded
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 120, 0, 150), 0, getHeight(), new Color(200, 60, 0, 180)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
    }
}

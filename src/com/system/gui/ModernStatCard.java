package com.system.gui;

import javax.swing.*;
import java.awt.*;
import com.system.models.DataManager;

public class ModernStatCard extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private final Color SUBTLE_GRAY = new Color(160, 175, 200);

    private JLabel lblTitle;
    private JLabel lblValue;

    /** Default constructor for WindowBuilder visual compatibility */
    public ModernStatCard() {
        this("TITLE", "VALUE");
    }

    public ModernStatCard(String title, String value) {
        setLayout(null);
        setOpaque(false);

        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(SUBTLE_GRAY);
        lblTitle.setBounds(20, 20, 150, 20);
        add(lblTitle);

        lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(Color.WHITE);
        lblValue.setBounds(20, 45, 150, 40);
        add(lblValue);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(SOFT_CARD_NAVY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(DataManager.SUNSET_ORANGE);
        g2.fillRoundRect(0, 25, 5, 50, 10, 10);
        g2.dispose();
    }
}

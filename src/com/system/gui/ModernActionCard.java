package com.system.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.system.models.DataManager;

public class ModernActionCard extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Color ACTION_WHITE = Color.WHITE;
    private final Color REFINED_NAVY = new Color(30, 45, 75);

    private JLabel lblTitle;
    private JLabel lblDesc;
    private RoundedButton btnAction;

    public ModernActionCard(String title, String desc, String btnText) {
        setLayout(null);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(REFINED_NAVY);
        lblTitle.setBounds(25, 22, 300, 25);
        add(lblTitle);

        lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(100, 110, 130));
        lblDesc.setBounds(25, 48, 400, 20);
        add(lblDesc);

        btnAction = new RoundedButton(btnText);
        btnAction.setRadius(12);
        btnAction.setDefaultColor(DataManager.SUNSET_ORANGE);
        btnAction.setHoverColor(new Color(255, 140, 30));
        btnAction.setPressedColor(new Color(200, 90, 0));
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAction.setForeground(Color.WHITE);
        btnAction.setBounds(435, 27, 130, 45);
        add(btnAction);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                btnAction.setBounds(getWidth() - 160, 30, 130, 45);
                lblDesc.setBounds(25, 48, getWidth() - 200, 20);
            }
        });
    }

    public void setButtonAction(ActionListener listener) {
        btnAction.addActionListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ACTION_WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
    }
}

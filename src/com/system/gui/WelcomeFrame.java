package com.system.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WelcomeFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Image bgImage;
	private RoundedButton btnNewButton;
	private JLabel lblSystemInfo;

	public WelcomeFrame() {
		contentPane = this;
		contentPane.setLayout(null);
	
		try {
			ImageIcon originalIcon = new ImageIcon(WelcomeFrame.class.getResource("/com/system/images/borrow (1).jpg"));
			bgImage = originalIcon.getImage();
		} catch (Exception e) {
			System.out.println("Background error: " + e.getMessage());
		}

		btnNewButton = new RoundedButton("Start Your Journey");
		btnNewButton.setRadius(0); // radius is calculated as height in RoundedButton if set to 0
		btnNewButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
		btnNewButton.setForeground(new Color(0, 102, 204)); 
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(e -> {
			MainDashboardFrame.showCard("DASHBOARD"); 
		});
	
		lblSystemInfo = new JLabel("Eroplanong Papel Terminal System v1.0.4 | Internal Staff Use Only");
		lblSystemInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblSystemInfo.setForeground(new Color(255, 255, 255, 100));
		contentPane.add(lblSystemInfo);
		
		contentPane.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int w = getWidth();
				int h = getHeight();
				// Center the button horizontally, and position it near the bottom third
				btnNewButton.setBounds((w - 260) / 2, h - 210, 260, 55);
				lblSystemInfo.setBounds(25, h - 65, 500, 20);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
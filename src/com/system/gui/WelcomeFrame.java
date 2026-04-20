package com.system.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.system.models.DataManager; // Import para mabasa yung MIDNIGHT_BLUE

public class WelcomeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeFrame frame = new WelcomeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WelcomeFrame() {
		setTitle("Eroplanong Papel - Welcome");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 450); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JButton btnNewButton = new JButton("Start Your Journey");
		btnNewButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btnNewButton.setBackground(DataManager.MIDNIGHT_BLUE);
		btnNewButton.setForeground(Color.WHITE);

		btnNewButton.setOpaque(true);
		btnNewButton.setContentAreaFilled(true);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false); 
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btnNewButton.setBounds(310, 310, 180, 45);
		contentPane.add(btnNewButton);

		// --- TRANSITION LOGIC ---
		btnNewButton.addActionListener(e -> {
		    new FlightViewFrame().setVisible(true); // Open next screen
		    this.dispose(); 
		});
	
		ImageIcon originalIcon = new ImageIcon(WelcomeFrame.class.getResource("/com/system/images/upscaled_malabo.png"));
		Image img = originalIcon.getImage();
		Image scaledImg = img.getScaledInstance(784, 411, Image.SCALE_SMOOTH);
		ImageIcon finalIcon = new ImageIcon(scaledImg); 

	
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(finalIcon); 
		lblBackground.setBounds(0, 0, 784, 411);
		contentPane.add(lblBackground);
	}
}

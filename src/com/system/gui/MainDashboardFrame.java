package com.system.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.system.models.DataManager; 

public class MainDashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainDashboardFrame frame = new MainDashboardFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainDashboardFrame() {
        setTitle("Eroplanong Papel - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 248)); // Subtle neutral background for main content
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // --- SIDEBAR (Clean Sunset Orange Gradient without planes) ---
        // Width: 230, Height: Full
        SidebarPanel pnlSidebar = new SidebarPanel();
        pnlSidebar.setBounds(0, 0, 230, 550);
        pnlSidebar.setLayout(null);
        contentPane.add(pnlSidebar);

        // --- SIDEBAR BUTTONS (View and Book Flights Only) ---
        // Clean white text to pop against the orange gradient
        JButton btnView = createSidebarButton("VIEW FLIGHTS");
        btnView.setBounds(15, 60, 200, 45);
        pnlSidebar.add(btnView);

        JButton btnBook = createSidebarButton("BOOK FLIGHTS");
        btnBook.setBounds(15, 120, 200, 45);
        pnlSidebar.add(btnBook);
        
        // Button Logic
        btnView.addActionListener(e -> { 
            new FlightViewFrame().setVisible(true); 
            this.dispose(); 
        });
        
        // --- MAIN CONTENT AREA ---
        // Ginamit ang RoundedPanel kung meron ka, kung wala standard JPanel
        // I-adjust ko ito sa standard JPanel para makasiguro na walang compilation error
        JPanel pnlMain = new JPanel();
        pnlMain.setBounds(230, 0, 620, 550);
        pnlMain.setBackground(new Color(245, 245, 248));
        pnlMain.setLayout(null);
        contentPane.add(pnlMain);
        
        JLabel lblWelcome = new JLabel("Welcome to your dashboard!");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblWelcome.setForeground(new Color(100, 100, 100));
        lblWelcome.setBounds(20, 20, 300, 30);
        pnlMain.add(lblWelcome);
    }

    // Helper method para sa buttons (Themed Glassmorphism)
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setForeground(Color.DARK_GRAY); // Default text color
        btn.setBackground(new Color(255, 255, 255, 180)); // Semi-transparent white frosting
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Hover effect: Pure White background, DataManager.SUNSET_ORANGE text
                btn.setBackground(Color.WHITE);
                btn.setForeground(DataManager.SUNSET_ORANGE); 
            }
            public void mouseExited(MouseEvent e) {
                // Back to standard frosted white, dark text
                btn.setBackground(new Color(255, 255, 255, 180));
                btn.setForeground(Color.DARK_GRAY);
            }
        });
        return btn;
    }

    // Custom class para sa Sidebar na may Advanced Gradient and Definition Border
    class SidebarPanel extends JPanel {
        
        public SidebarPanel() {
            // No image loading logic needed
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Ginamit ang GradientPaint para sa "Biyaheng Langit" sunset feel
            // Upper: Rich Orange (DataManager.SUNSET_ORANGE), Lower: Warmer Golden Orange
            Color startColor = DataManager.SUNSET_ORANGE; 
            Color endColor = new Color(255, 170, 85); // A warm golden orange
            
            GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Subtle premium glassmorphism lens effect near the top-left edge
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(5, 5, getWidth() / 3, getHeight() / 2, 0, 0);

            // Fine modern definition border outline (dark outline)
            g2.setColor(new Color(0, 0, 0, 30)); // subtle fine dark outline
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 0, 0);

            g2.dispose();
        }
    }
}
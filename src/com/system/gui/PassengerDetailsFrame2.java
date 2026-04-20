package com.system.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.system.models.DataManager;
import java.awt.*;

public class PassengerDetailsFrame2 extends JFrame {

    private JPanel contentPane;
    private JTextField txtName, txtAge, txtContact, txtPassport;

    public PassengerDetailsFrame2() {
        setTitle("Eroplanong Papel - Passenger Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(0, 51, 102));
        pnlHeader.setBounds(0, 0, 850, 80);
        contentPane.add(pnlHeader);
        pnlHeader.setLayout(null);

        JLabel lblTitle = new JLabel("PASSENGER DETAILS");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(30, 20, 300, 40);
        pnlHeader.add(lblTitle);

        // --- FLIGHT SUMMARY ---
        String flightInfo = (DataManager.selectedFlightData != null) 
            ? "Flight: " + DataManager.selectedFlightData[1] + " | Price: " + DataManager.selectedFlightData[2]
            : "No Flight Selected";
        JLabel lblSummary = new JLabel(flightInfo);
        lblSummary.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSummary.setBounds(30, 95, 600, 25);
        contentPane.add(lblSummary);

        // --- LABELS & FIELDS ---
        JLabel lbl1 = new JLabel("Full Name:");
        lbl1.setBounds(30, 140, 150, 25);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contentPane.add(lbl1);
        txtName = new JTextField();
        txtName.setBounds(30, 165, 350, 35);
        contentPane.add(txtName);

        JLabel lbl2 = new JLabel("Contact Number:");
        lbl2.setBounds(30, 215, 150, 25);
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contentPane.add(lbl2);
        txtContact = new JTextField();
        txtContact.setBounds(30, 240, 350, 35);
        contentPane.add(txtContact);

        JLabel lbl3 = new JLabel("Passport Number:");
        lbl3.setBounds(30, 290, 150, 25);
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contentPane.add(lbl3);
        txtPassport = new JTextField();
        txtPassport.setBounds(30, 315, 350, 35);
        contentPane.add(txtPassport);

        JLabel lbl4 = new JLabel("Age:");
        lbl4.setBounds(420, 140, 150, 25);
        lbl4.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contentPane.add(lbl4);
        txtAge = new JTextField();
        txtAge.setBounds(420, 165, 100, 35);
        contentPane.add(txtAge);

        // --- ACTION BUTTONS ---
        JButton btnConfirm = new JButton("CONFIRM & PROCEED");
        btnConfirm.setBackground(DataManager.SUNSET_ORANGE);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorderPainted(false); // Para mas flat at clean
        btnConfirm.setBounds(600, 430, 200, 45);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnConfirm);

        JButton btnBack = new JButton("BACK");
        btnBack.setBounds(30, 430, 100, 45);
        btnBack.setFocusPainted(false);
        contentPane.add(btnBack);

        // --- LOGIC ---
        
        // Pag-click ng Back, balik sa Flight Selection
        btnBack.addActionListener(e -> { 
            new FlightViewFrame().setVisible(true); 
            this.dispose(); 
        });
        
        // Pag-click ng Confirm, save logic
        btnConfirm.addActionListener(e -> { 
            if(txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter passenger name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Booking details saved for " + txtName.getText() + "!");
       
            }
    }
}

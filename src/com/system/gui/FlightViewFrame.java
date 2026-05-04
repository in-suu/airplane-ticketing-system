package com.system.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;

public class FlightViewFrame extends JFrame {

    public FlightViewFrame() {
        setTitle("Eroplanong Papel - Flight Selection");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(DataManager.MIDNIGHT_BLUE);
        pnlHeader.setBounds(0, 0, 850, 70);
        pnlHeader.setLayout(null);
        getContentPane().add(pnlHeader);

        JLabel lblTitle = new JLabel("AVAILABLE FLIGHTS");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(DataManager.HEADER_FONT);
        lblTitle.setBounds(30, 15, 300, 40);
        pnlHeader.add(lblTitle);

        // --- SEARCH BAR ---
        JLabel lblSearch = new JLabel("Search Destination:");
        lblSearch.setBounds(30, 90, 150, 25);
        getContentPane().add(lblSearch);

        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(160, 90, 250, 30);
        getContentPane().add(txtSearch);

        // --- DYNAMIC TABLE ---
        String[] cols = {"ID", "Destination", "Departure", "Arrival", "Price"};
        DefaultTableModel model = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        model.addRow(new Object[]{"EP-01", "Manila to Japan", "08:00 AM", "01:00 PM", "P12,000"});
        model.addRow(new Object[]{"EP-02", "Manila to Korea", "10:00 AM", "02:00 PM", "P10,500"});

        JTable tbl = new JTable(model);
        tbl.setRowHeight(35);
        tbl.getTableHeader().setBackground(DataManager.MIDNIGHT_BLUE);
        tbl.getTableHeader().setForeground(Color.WHITE);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl.setRowSorter(sorter);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtSearch.getText(), 1));
            }
        });

        JScrollPane sp = new JScrollPane(tbl);
        sp.setBounds(30, 130, 770, 280);
        getContentPane().add(sp);

        // --- BUTTONS ---
        JButton btnBook = new JButton("BOOK FLIGHT");
        btnBook.setBackground(DataManager.SUNSET_ORANGE);
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);
        btnBook.setBounds(620, 430, 180, 45);
        getContentPane().add(btnBook);
        btnBook.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                int modelRow = tbl.convertRowIndexToModel(row);
                DataManager.selectedFlightData = new Object[] {
                    model.getValueAt(modelRow, 0),
                    model.getValueAt(modelRow, 1),
                    model.getValueAt(modelRow, 4)
                };
                JOptionPane.showMessageDialog(null, "Flight Selected: " + DataManager.selectedFlightData[1]);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a flight first!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnBack = new JButton("BACK");
        btnBack.setBounds(30, 430, 100, 45);
        getContentPane().add(btnBack);
        btnBack.addActionListener(e -> { new WelcomeFrame().setVisible(true); this.dispose(); });

        // --- BACKGROUND IMAGE ---
        try {
            // FIX: Inalis ang /src/ at idinagdag ang /images/ sa path
            ImageIcon bgIcon = new ImageIcon(getClass().getResource("/com/system/images/borrow(1).jpg"));
            Image img = bgIcon.getImage().getScaledInstance(850, 550, Image.SCALE_SMOOTH);
            JLabel lblBackground = new JLabel(new ImageIcon(img));
            lblBackground.setBounds(0, 0, 850, 550);
            getContentPane().add(lblBackground);
        } catch (Exception e) {
            System.out.println("Image Error: " + e.getMessage());
        }
    }
}
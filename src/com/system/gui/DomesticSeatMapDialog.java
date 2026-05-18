package com.system.gui;

import javax.swing.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;

public class DomesticSeatMapDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private String selectedSeat = "None";
    private final String[] tempSelected = {"None"};
    private boolean isBusinessMode = false;

    private final Color REFINED_NAVY = new Color(30, 45, 75);
    private final Color SUBTLE_GRAY  = new Color(160, 175, 200);

    // ── Instance fields (WindowBuilder friendly) ─────────────────────────────
    private JPanel  pnlContent;
    private JLabel  lblHeader;
    private JPanel  pnlLegend;
    private JPanel  pnlGridHeader;
    private JLabel  lblNose;
    private JPanel  pnlRows;
    private JLabel  lblClassNotice;
    private JButton btnConfirmSeat;

    // Row panels
    private JPanel pnlRow1, pnlRow2, pnlRow3, pnlRow4;
    private JPanel pnlRow5, pnlRow6, pnlRow7, pnlRow8;

    // Row number labels
    private JLabel lblRow1, lblRow2, lblRow3, lblRow4;
    private JLabel lblRow5, lblRow6, lblRow7, lblRow8;

    // Seat buttons (row × col A-F, no I)
    private JButton s1A, s1B, s1C, s1D, s1E, s1F;
    private JButton s2A, s2B, s2C, s2D, s2E, s2F;
    private JButton s3A, s3B, s3C, s3D, s3E, s3F;
    private JButton s4A, s4B, s4C, s4D, s4E, s4F;
    private JButton s5A, s5B, s5C, s5D, s5E, s5F;
    private JButton s6A, s6B, s6C, s6D, s6E, s6F;
    private JButton s7A, s7B, s7C, s7D, s7E, s7F;
    private JButton s8A, s8B, s8C, s8D, s8E, s8F;

    /** No-arg constructor for WindowBuilder */
    public DomesticSeatMapDialog() {
        this(null, false, "None");
    }

    private java.util.ArrayList<String> occupiedSeats = new java.util.ArrayList<>();

    public DomesticSeatMapDialog(Frame owner, boolean isBusinessMode, String currentSeat) {
        super(owner, "Seats Availability Matrix", true);
        this.isBusinessMode = isBusinessMode;
        this.tempSelected[0] = currentSeat;
        
        // Fetch occupied seats dynamically from DataManager using active selected flight ID!
        if (DataManager.selectedFlightData != null) {
            String flightId = DataManager.selectedFlightData[0].toString();
            this.occupiedSeats = DataManager.getOccupiedSeats(flightId);
        }
        
        initComponents();
        initSeatStyles();
    }

    private void initComponents() {
        setSize(480, 580);
        setResizable(false);
        setLocationRelativeTo(null);

        pnlContent = new JPanel(null);
        pnlContent.setBackground(Color.WHITE);
        setContentPane(pnlContent);

        lblHeader = new JLabel("Seat Assignment", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(REFINED_NAVY);
        lblHeader.setBounds(0, 10, 480, 25);
        pnlContent.add(lblHeader);

        // ── Legend ────────────────────────────────────────────────────────────
        pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlLegend.setBackground(Color.WHITE);
        pnlLegend.setBounds(0, 45, 480, 30);
        addLegendItem(pnlLegend, "Selected",      DataManager.SUNSET_ORANGE, true);
        addLegendItem(pnlLegend, "Occupied",       new Color(195, 125, 30),  true);
        addLegendItem(pnlLegend, "Business Open",  new Color(212, 160, 23),  false);
        addLegendItem(pnlLegend, "Economy Open",   new Color(0, 150, 215),   false);
        pnlContent.add(pnlLegend);

        // ── Column headers ────────────────────────────────────────────────────
        pnlGridHeader = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlGridHeader.setBounds(45, 85, 375, 25);
        pnlGridHeader.setBackground(Color.WHITE);
        pnlGridHeader.add(new JLabel("A", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("B", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("C", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("I", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("D", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("E", SwingConstants.CENTER));
        pnlGridHeader.add(new JLabel("F", SwingConstants.CENTER));
        pnlContent.add(pnlGridHeader);

        lblNose = new JLabel("\u25B2 FRONT OF AIRCRAFT \u25B2", SwingConstants.CENTER);
        lblNose.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblNose.setForeground(SUBTLE_GRAY);
        lblNose.setBounds(45, 105, 375, 15);
        pnlContent.add(lblNose);

        // ── Seat rows container ───────────────────────────────────────────────
        pnlRows = new JPanel(new GridLayout(8, 1, 0, 6));
        pnlRows.setBounds(45, 120, 375, 300);
        pnlRows.setBackground(Color.WHITE);
        pnlContent.add(pnlRows);

        // --- Row 1 ---
        pnlRow1 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow1.setBackground(Color.WHITE);
        s1A = new JButton(); pnlRow1.add(s1A);
        s1B = new JButton(); pnlRow1.add(s1B);
        s1C = new JButton(); pnlRow1.add(s1C);
        lblRow1 = new JLabel("1", SwingConstants.CENTER);
        lblRow1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow1.add(lblRow1);
        s1D = new JButton(); pnlRow1.add(s1D);
        s1E = new JButton(); pnlRow1.add(s1E);
        s1F = new JButton(); pnlRow1.add(s1F);
        pnlRows.add(pnlRow1);

        // --- Row 2 ---
        pnlRow2 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow2.setBackground(Color.WHITE);
        s2A = new JButton(); pnlRow2.add(s2A);
        s2B = new JButton(); pnlRow2.add(s2B);
        s2C = new JButton(); pnlRow2.add(s2C);
        lblRow2 = new JLabel("2", SwingConstants.CENTER);
        lblRow2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow2.add(lblRow2);
        s2D = new JButton(); pnlRow2.add(s2D);
        s2E = new JButton(); pnlRow2.add(s2E);
        s2F = new JButton(); pnlRow2.add(s2F);
        pnlRows.add(pnlRow2);

        // --- Row 3 ---
        pnlRow3 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow3.setBackground(Color.WHITE);
        s3A = new JButton(); pnlRow3.add(s3A);
        s3B = new JButton(); pnlRow3.add(s3B);
        s3C = new JButton(); pnlRow3.add(s3C);
        lblRow3 = new JLabel("3", SwingConstants.CENTER);
        lblRow3.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow3.add(lblRow3);
        s3D = new JButton(); pnlRow3.add(s3D);
        s3E = new JButton(); pnlRow3.add(s3E);
        s3F = new JButton(); pnlRow3.add(s3F);
        pnlRows.add(pnlRow3);

        // --- Row 4 ---
        pnlRow4 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow4.setBackground(Color.WHITE);
        s4A = new JButton(); pnlRow4.add(s4A);
        s4B = new JButton(); pnlRow4.add(s4B);
        s4C = new JButton(); pnlRow4.add(s4C);
        lblRow4 = new JLabel("4", SwingConstants.CENTER);
        lblRow4.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow4.add(lblRow4);
        s4D = new JButton(); pnlRow4.add(s4D);
        s4E = new JButton(); pnlRow4.add(s4E);
        s4F = new JButton(); pnlRow4.add(s4F);
        pnlRows.add(pnlRow4);

        // --- Row 5 ---
        pnlRow5 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow5.setBackground(Color.WHITE);
        s5A = new JButton(); pnlRow5.add(s5A);
        s5B = new JButton(); pnlRow5.add(s5B);
        s5C = new JButton(); pnlRow5.add(s5C);
        lblRow5 = new JLabel("5", SwingConstants.CENTER);
        lblRow5.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow5.add(lblRow5);
        s5D = new JButton(); pnlRow5.add(s5D);
        s5E = new JButton(); pnlRow5.add(s5E);
        s5F = new JButton(); pnlRow5.add(s5F);
        pnlRows.add(pnlRow5);

        // --- Row 6 ---
        pnlRow6 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow6.setBackground(Color.WHITE);
        s6A = new JButton(); pnlRow6.add(s6A);
        s6B = new JButton(); pnlRow6.add(s6B);
        s6C = new JButton(); pnlRow6.add(s6C);
        lblRow6 = new JLabel("6", SwingConstants.CENTER);
        lblRow6.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow6.add(lblRow6);
        s6D = new JButton(); pnlRow6.add(s6D);
        s6E = new JButton(); pnlRow6.add(s6E);
        s6F = new JButton(); pnlRow6.add(s6F);
        pnlRows.add(pnlRow6);

        // --- Row 7 ---
        pnlRow7 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow7.setBackground(Color.WHITE);
        s7A = new JButton(); pnlRow7.add(s7A);
        s7B = new JButton(); pnlRow7.add(s7B);
        s7C = new JButton(); pnlRow7.add(s7C);
        lblRow7 = new JLabel("7", SwingConstants.CENTER);
        lblRow7.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow7.add(lblRow7);
        s7D = new JButton(); pnlRow7.add(s7D);
        s7E = new JButton(); pnlRow7.add(s7E);
        s7F = new JButton(); pnlRow7.add(s7F);
        pnlRows.add(pnlRow7);

        // --- Row 8 ---
        pnlRow8 = new JPanel(new GridLayout(1, 7, 8, 0));
        pnlRow8.setBackground(Color.WHITE);
        s8A = new JButton(); pnlRow8.add(s8A);
        s8B = new JButton(); pnlRow8.add(s8B);
        s8C = new JButton(); pnlRow8.add(s8C);
        lblRow8 = new JLabel("8", SwingConstants.CENTER);
        lblRow8.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pnlRow8.add(lblRow8);
        s8D = new JButton(); pnlRow8.add(s8D);
        s8E = new JButton(); pnlRow8.add(s8E);
        s8F = new JButton(); pnlRow8.add(s8F);
        pnlRows.add(pnlRow8);

        // ── Class notice ──────────────────────────────────────────────────────
        lblClassNotice = new JLabel(
            isBusinessMode ? "\u2728 Single-Aisle: Business Class (Rows 1-2 Only)"
                           : "\u2708 Single-Aisle: Economy Class (Rows 3-8 Only)",
            SwingConstants.CENTER);
        lblClassNotice.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 11));
        lblClassNotice.setForeground(isBusinessMode ? new Color(175, 130, 15) : new Color(0, 110, 180));
        lblClassNotice.setBounds(0, 440, 480, 25);
        pnlContent.add(lblClassNotice);

        // ── Confirm button ────────────────────────────────────────────────────
        btnConfirmSeat = new JButton("CONFIRM SELECTION");
        btnConfirmSeat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmSeat.setForeground(Color.WHITE);
        btnConfirmSeat.setBackground(DataManager.SUNSET_ORANGE);
        btnConfirmSeat.setOpaque(true);
        btnConfirmSeat.setBorderPainted(false);
        btnConfirmSeat.setFocusPainted(false);
        btnConfirmSeat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmSeat.setBounds(130, 485, 220, 40);
        btnConfirmSeat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempSelected[0].equals("None")) {
                    JOptionPane.showMessageDialog(DomesticSeatMapDialog.this,
                        "Please select a seat first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
                } else {
                    selectedSeat = tempSelected[0];
                    dispose();
                }
            }
        });
        pnlContent.add(btnConfirmSeat);
    }

    public String getSelectedSeat() { return selectedSeat; }

    // ── Seat styling & configuration engine ──────────────────────────────────
    private void initSeatStyles() {
        configureSeat(s1A, 1, 'A'); configureSeat(s1B, 1, 'B'); configureSeat(s1C, 1, 'C');
        configureSeat(s1D, 1, 'D'); configureSeat(s1E, 1, 'E'); configureSeat(s1F, 1, 'F');

        configureSeat(s2A, 2, 'A'); configureSeat(s2B, 2, 'B'); configureSeat(s2C, 2, 'C');
        configureSeat(s2D, 2, 'D'); configureSeat(s2E, 2, 'E'); configureSeat(s2F, 2, 'F');

        configureSeat(s3A, 3, 'A'); configureSeat(s3B, 3, 'B'); configureSeat(s3C, 3, 'C');
        configureSeat(s3D, 3, 'D'); configureSeat(s3E, 3, 'E'); configureSeat(s3F, 3, 'F');

        configureSeat(s4A, 4, 'A'); configureSeat(s4B, 4, 'B'); configureSeat(s4C, 4, 'C');
        configureSeat(s4D, 4, 'D'); configureSeat(s4E, 4, 'E'); configureSeat(s4F, 4, 'F');

        configureSeat(s5A, 5, 'A'); configureSeat(s5B, 5, 'B'); configureSeat(s5C, 5, 'C');
        configureSeat(s5D, 5, 'D'); configureSeat(s5E, 5, 'E'); configureSeat(s5F, 5, 'F');

        configureSeat(s6A, 6, 'A'); configureSeat(s6B, 6, 'B'); configureSeat(s6C, 6, 'C');
        configureSeat(s6D, 6, 'D'); configureSeat(s6E, 6, 'E'); configureSeat(s6F, 6, 'F');

        configureSeat(s7A, 7, 'A'); configureSeat(s7B, 7, 'B'); configureSeat(s7C, 7, 'C');
        configureSeat(s7D, 7, 'D'); configureSeat(s7E, 7, 'E'); configureSeat(s7F, 7, 'F');

        configureSeat(s8A, 8, 'A'); configureSeat(s8B, 8, 'B'); configureSeat(s8C, 8, 'C');
        configureSeat(s8D, 8, 'D'); configureSeat(s8E, 8, 'E'); configureSeat(s8F, 8, 'F');
    }

    private void configureSeat(JButton btn, final int row, final char col) {
        final String seatID = row + String.valueOf(col);
        
        int capacity = 48;
        if (DataManager.selectedFlightData != null && DataManager.selectedFlightData.length > 10) {
            try {
                capacity = Integer.parseInt(DataManager.selectedFlightData[10].toString());
            } catch (Exception ex) {}
        }
        
        int colIndex = col - 'A';
        int linearIndex = (row - 1) * 6 + colIndex + 1;
        boolean outOfCapacity = linearIndex > capacity;

        if (outOfCapacity) {
            btn.setVisible(false);
            return;
        } else {
            btn.setVisible(true);
        }

        boolean occupied    = isOccupied(row, col);
        boolean isBizSeat   = (row <= 2);
        boolean wrongClass  = (isBusinessMode && !isBizSeat) || (!isBusinessMode && isBizSeat);

        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (occupied) {
            btn.setBackground(new Color(195, 125, 30));
            btn.setBorderPainted(false);
            btn.setEnabled(false);
            btn.setToolTipText("Seat " + seatID + " | OCCUPIED");
        } else if (wrongClass) {
            btn.setBackground(new Color(242, 244, 247));
            btn.setBorder(BorderFactory.createLineBorder(new Color(215, 220, 225), 1));
            btn.setEnabled(false);
            btn.setToolTipText("Seat " + seatID + " | Locked");
        } else if (seatID.equals(tempSelected[0])) {
            btn.setBackground(DataManager.SUNSET_ORANGE);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createLineBorder(
                isBizSeat ? new Color(212, 160, 23) : new Color(0, 150, 215), 2));
        }

        if (!occupied && !wrongClass) {
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tempSelected[0] = seatID;
                    refreshSeats();
                }
            });
        }
    }

    private void refreshSeats() {
        initSeatStyles();
        pnlRows.repaint();
    }

    private boolean isOccupied(int row, char col) {
        String seatId1 = row + String.valueOf(col);
        String seatId2 = String.format("%02d%c", row, col);
        return occupiedSeats.contains(seatId1.toUpperCase()) || occupiedSeats.contains(seatId2.toUpperCase());
    }

    private void addLegendItem(JPanel parent, String text, Color color, boolean filled) {
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(14, 14));
        box.setBackground(filled ? color : Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(color, 2));
        parent.add(box);
        JLabel lbl = new JLabel("=" + text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(60, 70, 90));
        parent.add(lbl);
    }
}

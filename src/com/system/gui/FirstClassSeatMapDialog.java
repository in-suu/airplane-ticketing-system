package com.system.gui;

import javax.swing.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;

public class FirstClassSeatMapDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private String selectedSeat = "None";
    private final String[] tempSelected = {"None"};
    private boolean isInternational = false;

    private final Color ROYAL_GOLD    = new Color(212, 175, 55); // #D4AF37
    private final Color REFINED_NAVY  = new Color(20, 30, 55);
    private final Color SUBTLE_GRAY   = new Color(150, 165, 185);
    private final Color DEEP_GOLD_BG  = new Color(255, 248, 220);

    private JPanel  pnlContent;
    private JLabel  lblHeader;
    private JPanel  pnlLegend;
    private JLabel  lblNose;
    private JPanel  pnlRows;
    private JLabel  lblClassNotice;
    private JButton btnConfirmSeat;

    // Row Panels
    private JPanel pnlRow1, pnlRow2;
    private JLabel lblRow1, lblRow2;

    // Premium Suite Buttons
    private JButton s1A, s1D, s1F;
    private JButton s2A, s2D, s2F;

    public FirstClassSeatMapDialog() {
        this(null, false, "None");
    }

    private java.util.ArrayList<String> occupiedSeats = new java.util.ArrayList<>();

    public FirstClassSeatMapDialog(Frame owner, boolean isInternational, String currentSeat) {
        super(owner, "First Class Royal Suites Matrix", true);
        this.isInternational = isInternational;
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
        setSize(480, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        pnlContent = new JPanel(null);
        pnlContent.setBackground(Color.WHITE);
        setContentPane(pnlContent);

        lblHeader = new JLabel("✨ Royal First Class Suite Map ✨", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(ROYAL_GOLD);
        lblHeader.setBounds(0, 15, 480, 25);
        pnlContent.add(lblHeader);

        // ── Legend ────────────────────────────────────────────────────────────
        pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlLegend.setBackground(Color.WHITE);
        pnlLegend.setBounds(0, 50, 480, 30);
        addLegendItem(pnlLegend, "Selected", DataManager.SUNSET_ORANGE, true);
        addLegendItem(pnlLegend, "Occupied", new Color(195, 125, 30), true);
        addLegendItem(pnlLegend, "Suite Available", ROYAL_GOLD, false);
        pnlContent.add(pnlLegend);

        lblNose = new JLabel("👑 FRONT OF FIRST CLASS CABIN 👑", SwingConstants.CENTER);
        lblNose.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNose.setForeground(ROYAL_GOLD);
        lblNose.setBounds(45, 95, 375, 15);
        pnlContent.add(lblNose);

        // ── Seat rows container ───────────────────────────────────────────────
        pnlRows = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlRows.setBounds(45, 125, 375, 180);
        pnlRows.setBackground(Color.WHITE);
        pnlContent.add(pnlRows);

        // Grid width: 5 slots (A, Space/D, Center, Space/D, F)
        // Row 1
        pnlRow1 = new JPanel(new GridLayout(1, 5, 12, 0));
        pnlRow1.setBackground(Color.WHITE);
        s1A = new JButton("1A"); pnlRow1.add(s1A);
        
        if (isInternational) {
            s1D = new JButton("1D"); pnlRow1.add(s1D);
        } else {
            pnlRow1.add(new JLabel("")); // Empty luxury aisle space
        }
        
        lblRow1 = new JLabel("Row 1", SwingConstants.CENTER);
        lblRow1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblRow1.setOpaque(true);
        lblRow1.setBackground(DEEP_GOLD_BG);
        lblRow1.setForeground(ROYAL_GOLD);
        lblRow1.setBorder(BorderFactory.createLineBorder(ROYAL_GOLD, 1));
        pnlRow1.add(lblRow1);

        pnlRow1.add(new JLabel("")); // Aisle
        s1F = new JButton("1F"); pnlRow1.add(s1F);
        pnlRows.add(pnlRow1);

        // Row 2
        pnlRow2 = new JPanel(new GridLayout(1, 5, 12, 0));
        pnlRow2.setBackground(Color.WHITE);
        s2A = new JButton("2A"); pnlRow2.add(s2A);
        
        if (isInternational) {
            s2D = new JButton("2D"); pnlRow2.add(s2D);
        } else {
            pnlRow2.add(new JLabel("")); // Empty luxury aisle space
        }
        
        lblRow2 = new JLabel("Row 2", SwingConstants.CENTER);
        lblRow2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblRow2.setOpaque(true);
        lblRow2.setBackground(DEEP_GOLD_BG);
        lblRow2.setForeground(ROYAL_GOLD);
        lblRow2.setBorder(BorderFactory.createLineBorder(ROYAL_GOLD, 1));
        pnlRow2.add(lblRow2);

        pnlRow2.add(new JLabel("")); // Aisle
        s2F = new JButton("2F"); pnlRow2.add(s2F);
        pnlRows.add(pnlRow2);

        // ── Class Notice ──────────────────────────────────────────────────────
        lblClassNotice = new JLabel(
            isInternational ? "✨ International Royal Suite Layout (Spacious 1-2-1 Suite Config)"
                            : "✨ Domestic Royal Suite Layout (Ultra-Exclusive 1-1 Private Config)",
            SwingConstants.CENTER);
        lblClassNotice.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 11));
        lblClassNotice.setForeground(ROYAL_GOLD);
        lblClassNotice.setBounds(0, 325, 480, 25);
        pnlContent.add(lblClassNotice);

        // ── Confirm Button ────────────────────────────────────────────────────
        btnConfirmSeat = new JButton("CONFIRM ROYAL SUITE");
        btnConfirmSeat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmSeat.setForeground(Color.WHITE);
        btnConfirmSeat.setBackground(ROYAL_GOLD);
        btnConfirmSeat.setOpaque(true);
        btnConfirmSeat.setBorderPainted(false);
        btnConfirmSeat.setFocusPainted(false);
        btnConfirmSeat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmSeat.setBounds(130, 375, 220, 40);
        btnConfirmSeat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempSelected[0].equals("None")) {
                    JOptionPane.showMessageDialog(FirstClassSeatMapDialog.this,
                        "Please select a royal suite first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
                } else {
                    selectedSeat = tempSelected[0];
                    dispose();
                }
            }
        });
        pnlContent.add(btnConfirmSeat);
    }

    public String getSelectedSeat() { return selectedSeat; }

    private void initSeatStyles() {
        configureSeat(s1A, 1, 'A');
        if (isInternational && s1D != null) configureSeat(s1D, 1, 'D');
        configureSeat(s1F, 1, 'F');

        configureSeat(s2A, 2, 'A');
        if (isInternational && s2D != null) configureSeat(s2D, 2, 'D');
        configureSeat(s2F, 2, 'F');
    }

    private void configureSeat(JButton btn, final int row, final char col) {
        final String seatID = row + String.valueOf(col);
        boolean occupied    = isOccupied(row, col);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (occupied) {
            btn.setBackground(new Color(195, 125, 30));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setEnabled(false);
            btn.setToolTipText("Royal Suite " + seatID + " | OCCUPIED");
        } else if (seatID.equals(tempSelected[0])) {
            btn.setBackground(DataManager.SUNSET_ORANGE);
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(DEEP_GOLD_BG);
            btn.setForeground(ROYAL_GOLD);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createLineBorder(ROYAL_GOLD, 2));
        }

        if (!occupied) {
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
        box.setBackground(filled ? color : DEEP_GOLD_BG);
        box.setBorder(BorderFactory.createLineBorder(color, 2));
        parent.add(box);
        JLabel lbl = new JLabel("=" + text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(60, 70, 90));
        parent.add(lbl);
    }
}

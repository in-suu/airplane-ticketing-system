package com.system.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.system.models.DataManager;
import java.awt.*;
import java.util.Date;

public class TicketDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Color APP_DARK_NAVY = new Color(20, 30, 55);
    private final Color ACCENT_ORANGE  = DataManager.SUNSET_ORANGE;

    public TicketDialog(Frame owner, String name, String flightId, String origin, String dest, 
                        String cabinClass, Date depDate, String depSeat, 
                        boolean isRoundTrip, Date retDate, String retSeat) {
        super(owner, "Official Boarding Passes", true);
        setResizable(false);
        initComponents(name, flightId, origin, dest, cabinClass, depDate, depSeat, isRoundTrip, retDate, retSeat);
    }

    private void initComponents(String name, String flightId, String origin, String dest, 
                                String cabinClass, Date depDate, String depSeat, 
                                boolean isRoundTrip, Date retDate, String retSeat) {
        
        // Dynamically compute dialog size to perfectly fit card panel without scrollbars:
        // One card is 760x250.
        // We set the dialog width to 860 (one-way) or 1660 (round-trip) to allow 50px generous margins on both sides.
        int dlgWidth = (isRoundTrip && retDate != null && retSeat != null) ? 1660 : 860;
        int dlgHeight = 410;
        
        setSize(dlgWidth, dlgHeight);
        setLocationRelativeTo(getParent());
        
        // Root Panel
        JPanel pnlRoot = new JPanel(new BorderLayout());
        pnlRoot.setBackground(APP_DARK_NAVY);
        pnlRoot.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(pnlRoot);

        // Header Panel
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        
        JLabel lblTitle = new JLabel("━ DIGITAL E-TICKETS & BOARDING PASSES ━", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblSub = new JLabel("Please save your official boarding passes. Safe travels!", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(175, 190, 215));
        pnlHeader.add(lblSub, BorderLayout.SOUTH);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlRoot.add(pnlHeader, BorderLayout.NORTH);

        // Center Panel for Tickets (Horizontal FlowLayout centered)
        JPanel pnlTickets = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlTickets.setOpaque(false);
        
        // Add Departure Pass
        BoardingPassCard depCard = new BoardingPassCard(name, flightId, origin, dest, cabinClass, depDate, depSeat, "DEPARTURE TICKET");
        pnlTickets.add(depCard);
        
        // Add Return Pass if Round Trip
        if (isRoundTrip && retDate != null && retSeat != null) {
            String retFlightId = flightId.replace("DEP", "RET").replace("INT", "RINT");
            BoardingPassCard retCard = new BoardingPassCard(name, retFlightId, dest, origin, cabinClass, retDate, retSeat, "RETURN TICKET");
            pnlTickets.add(retCard);
        }
        
        pnlRoot.add(pnlTickets, BorderLayout.CENTER);

        // Footer Action Button
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlFooter.setOpaque(false);
        
        JButton btnClose = new JButton("DONE & SAVE TICKETS");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(ACCENT_ORANGE);
        btnClose.setPreferredSize(new Dimension(240, 42));
        btnClose.setOpaque(true);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        pnlFooter.add(btnClose);
        
        pnlRoot.add(pnlFooter, BorderLayout.SOUTH);
    }
}

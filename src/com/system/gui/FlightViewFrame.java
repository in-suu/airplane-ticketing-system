package com.system.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;

public class FlightViewFrame extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color REFINED_NAVY   = new Color(30, 45, 75);
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private final Color TEXT_WHITE     = Color.WHITE;
    private final Color SUBTLE_GRAY    = new Color(160, 175, 200);

    // ── All components as instance fields (required for WindowBuilder) ────────
    private SidebarPanel     pnlSidebar;
    private NavSidebarButton btnNavDashboard;
    private NavSidebarButton btnNavViewFlights;
    private NavSidebarButton btnNavBookFlights;
    private NavSidebarButton btnNavRecords;
    private NavSidebarButton btnNavExit;

    private JPanel           pnlMainBackground;
    private RoundedPanel     pnlMain;
    private JLabel           lblTitle;
    private JLabel           lblSearch;

    private JComboBox<String> cbFlightStatus;
    private JComboBox<String> cbFlightCategory;
    private JTextField        txtSearch;
    private JLabel            lblDate;
    private com.toedter.calendar.JDateChooser dcDateFilter;

    private JTable            tblFlights;
    private DefaultTableModel tableModel;
    private JScrollPane       scrollPane;

    private JPanel  pnlSelectedStrip;
    private JLabel  lblSelectedFlight;

    private ActionButton btnCancel;
    private ActionButton btnProceed;

    // ── Constructor ──────────────────────────────────────────────────────────
    public FlightViewFrame() {
        setLayout(new BorderLayout());
        setBackground(REFINED_NAVY);
        setPreferredSize(new Dimension(1250, 750)); // Design-time canvas size for WindowBuilder

        // ── Sidebar ──────────────────────────────────────────────────────────
        pnlSidebar = new SidebarPanel();
        pnlSidebar.setBackgroundImagePath("/com/system/images/Untitled design.jpg");
        pnlSidebar.setPreferredSize(new Dimension(280, 0));
        pnlSidebar.setLayout(null);
        add(pnlSidebar, BorderLayout.WEST);

        btnNavDashboard = new NavSidebarButton("DASHBOARD");
        btnNavDashboard.setBounds(20, 80, 240, 45);
        pnlSidebar.add(btnNavDashboard);

        btnNavViewFlights = new NavSidebarButton("VIEW FLIGHTS");
        btnNavViewFlights.setBounds(20, 135, 240, 45);
        btnNavViewFlights.setActive(true);
        pnlSidebar.add(btnNavViewFlights);

        btnNavBookFlights = new NavSidebarButton("BOOK FLIGHTS");
        btnNavBookFlights.setBounds(20, 190, 240, 45);
        pnlSidebar.add(btnNavBookFlights);

        btnNavRecords = new NavSidebarButton("RECORDS");
        btnNavRecords.setBounds(20, 245, 240, 45);
        pnlSidebar.add(btnNavRecords);

        btnNavExit = new NavSidebarButton("EXIT");
        btnNavExit.setBounds(20, 460, 240, 45);
        pnlSidebar.add(btnNavExit);

        pnlSidebar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                btnNavExit.setBounds(20, pnlSidebar.getHeight() - 80, 240, 45);
            }
        });

        btnNavDashboard.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { MainDashboardFrame.showCard("DASHBOARD"); }
        });
        btnNavViewFlights.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { MainDashboardFrame.showCard("FLIGHT_VIEW"); }
        });
        btnNavBookFlights.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { MainDashboardFrame.showCard("PASSENGER_DETAILS"); }
        });
        btnNavRecords.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { MainDashboardFrame.showCard("RECORDS_VIEW"); }
        });
        btnNavExit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showExitDialog(); }
        });

        // ── Main background ──────────────────────────────────────────────────
        pnlMainBackground = new JPanel(null);
        pnlMainBackground.setBackground(REFINED_NAVY);
        add(pnlMainBackground, BorderLayout.CENTER);

        pnlMain = new RoundedPanel();
        pnlMain.setCornerRadius(25);
        pnlMain.setBackground(SOFT_CARD_NAVY);
        pnlMain.setLayout(null);
        // Initial bounds (pw=890, ph=630) — updated at runtime by componentResized
        pnlMain.setBounds(40, 40, 890, 630);
        pnlMainBackground.add(pnlMain);

        // ── Title ────────────────────────────────────────────────────────────
        lblTitle = new JLabel("Available Flights");
        lblTitle.setForeground(TEXT_WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(40, 30, 400, 30);
        pnlMain.add(lblTitle);

        // ── Filters ──────────────────────────────────────────────────────────
        cbFlightStatus = new JComboBox<>(new String[]{"All Statuses", "Available", "Delayed", "Cancelled", "Fully Booked"});
        cbFlightStatus.setBounds(40, 80, 150, 40);
        cbFlightStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFlightStatus.setBackground(Color.WHITE);
        cbFlightStatus.setFocusable(false);
        pnlMain.add(cbFlightStatus);

        cbFlightCategory = new JComboBox<>(new String[]{"All Flights", "Domestic", "International"});
        cbFlightCategory.setBounds(200, 80, 140, 40);
        cbFlightCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFlightCategory.setBackground(Color.WHITE);
        cbFlightCategory.setFocusable(false);
        pnlMain.add(cbFlightCategory);

        lblDate = new JLabel("Date:");
        lblDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblDate.setForeground(SUBTLE_GRAY);
        lblDate.setBounds(350, 80, 40, 40);
        pnlMain.add(lblDate);

        dcDateFilter = new com.toedter.calendar.JDateChooser();
        dcDateFilter.setBounds(390, 80, 140, 40);
        dcDateFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dcDateFilter.setBackground(Color.WHITE);
        dcDateFilter.setMinSelectableDate(new java.util.Date());
        java.util.Calendar maxCal = java.util.Calendar.getInstance();
        maxCal.add(java.util.Calendar.MONTH, 3);
        dcDateFilter.setMaxSelectableDate(maxCal.getTime());
        pnlMain.add(dcDateFilter);

        lblSearch = new JLabel("Search Keyword:");
        lblSearch.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSearch.setForeground(SUBTLE_GRAY);
        lblSearch.setBounds(540, 80, 90, 40);
        pnlMain.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(630, 80, 220, 40);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        pnlMain.add(txtSearch);

        // ── Table ────────────────────────────────────────────────────────────
        String[] columns = {"Flight No.", "Origin", "Destination", "Date", "Time (Departure)", "Seats Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblFlights = new JTable(tableModel);
        tblFlights.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        tblFlights.setRowHeight(32);
        tblFlights.setGridColor(new Color(235, 240, 245));
        tblFlights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFlights.setRowSelectionAllowed(true);
        tblFlights.setColumnSelectionAllowed(false);
        tblFlights.setCellSelectionEnabled(false);
        tblFlights.setShowGrid(true);

        JTableHeader header = tblFlights.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(60, 75, 100));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                if (c instanceof JLabel) {
                    JLabel lbl = (JLabel) c;
                    lbl.setOpaque(true);
                    lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    lbl.setBackground(isSelected ? new Color(210, 225, 245) : Color.WHITE);
                    if (column == 6 && value != null) {
                        String s = value.toString().toUpperCase();
                        if      (s.contains("AVAIL"))   lbl.setForeground(new Color(20, 140, 60));
                        else if (s.contains("FULL") || s.contains("CANCEL")) lbl.setForeground(new Color(220, 40, 40));
                        else if (s.contains("DELAY"))   lbl.setForeground(new Color(235, 110, 0));
                        else lbl.setForeground(isSelected ? REFINED_NAVY : new Color(50, 60, 75));
                    } else {
                        lbl.setForeground(isSelected ? REFINED_NAVY : new Color(50, 60, 75));
                    }
                }
                return c;
            }
        };
        for (int i = 0; i < 7; i++) {
            tblFlights.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
        }

        scrollPane = new JScrollPane(tblFlights);
        scrollPane.setBounds(40, 140, 810, 340);  // pw-80=810, ph-290=340
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(215, 220, 225), 1));
        pnlMain.add(scrollPane);

        // ── Selected flight strip ────────────────────────────────────────────
        pnlSelectedStrip = new JPanel(null);
        pnlSelectedStrip.setBackground(Color.WHITE);
        pnlSelectedStrip.setBounds(40, 500, 810, 40);  // ph-130=500
        pnlMain.add(pnlSelectedStrip);

        lblSelectedFlight = new JLabel("No flight selected. Please choose a flight from the table.");
        lblSelectedFlight.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblSelectedFlight.setForeground(new Color(50, 65, 90));
        lblSelectedFlight.setBounds(15, 0, 780, 40);  // pw-110=780
        pnlSelectedStrip.add(lblSelectedFlight);

        // ── Bottom buttons ───────────────────────────────────────────────────
        btnCancel = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnCancel.setBounds(40, 550, 395, 45);  // ph-80=550, (pw-100)/2=395
        btnCancel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        pnlMain.add(btnCancel);

        btnProceed = new ActionButton("PROCEED TO BOOKING", DataManager.SUNSET_ORANGE, Color.WHITE);
        btnProceed.setBounds(455, 550, 395, 45);  // 40+395+20=455
        pnlMain.add(btnProceed);

        // ── Dynamic resize ───────────────────────────────────────────────────
        pnlMainBackground.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = pnlMainBackground.getWidth();
                int h = pnlMainBackground.getHeight();
                pnlMain.setBounds(40, 40, w - 80, h - 80);
                int pw = pnlMain.getWidth();
                int ph = pnlMain.getHeight();
                
                cbFlightStatus.setBounds(40, 80, 150, 40);
                cbFlightCategory.setBounds(200, 80, 140, 40);
                
                lblDate.setBounds(pw - 540, 80, 40, 40);
                dcDateFilter.setBounds(pw - 500, 80, 140, 40);
                lblSearch.setBounds(pw - 350, 80, 90, 40);
                txtSearch.setBounds(pw - 260, 80, 220, 40);
                
                scrollPane.setBounds(40, 140, pw - 80, ph - 290);
                pnlSelectedStrip.setBounds(40, ph - 130, pw - 80, 40);
                lblSelectedFlight.setBounds(15, 0, pw - 110, 40);
                btnCancel .setBounds(40,                              ph - 80, (pw - 100) / 2, 45);
                btnProceed.setBounds(40 + ((pw - 100) / 2) + 20,     ph - 80, (pw - 100) / 2, 45);
            }
        });

        // ── Event listeners ──────────────────────────────────────────────────
        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override public void ancestorAdded(javax.swing.event.AncestorEvent ev)   { loadFlightRecordsData(); }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent ev) {}
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent ev)   {}
        });

        cbFlightStatus.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { loadFlightRecordsData(); }
        });
        cbFlightCategory.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { loadFlightRecordsData(); }
        });
        dcDateFilter.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                loadFlightRecordsData();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { loadFlightRecordsData(); }
        });

        tblFlights.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                int row = tblFlights.getSelectedRow();
                if (row != -1) {
                    String id   = tblFlights.getValueAt(row, 0).toString();
                    String dest = tblFlights.getValueAt(row, 2).toString();
                    String date = tblFlights.getValueAt(row, 3).toString();
                    lblSelectedFlight.setText("Selected: MNL - " + dest + " (" + date + ") [" + id + "]");
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { MainDashboardFrame.showCard("DASHBOARD"); }
        });

        btnProceed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblFlights.getSelectedRow();
                Object[] rawFlight = null;

                if (selectedRow != -1) {
                    String flightId = tblFlights.getValueAt(selectedRow, 0).toString();
                    for (Object[] f : DataManager.getFlightsDB()) {
                        if (f[0].toString().equals(flightId)) { rawFlight = f; break; }
                    }
                    if (rawFlight != null) {
                        String status = rawFlight[5].toString().toUpperCase();
                        if (status.contains("FULL")) {
                            JOptionPane.showMessageDialog(FlightViewFrame.this,
                                "<html><font color='red'><b>Flight Full:</b></font><br>This flight is fully booked.</html>",
                                "Booking Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (status.contains("CANCEL")) {
                            JOptionPane.showMessageDialog(FlightViewFrame.this,
                                "<html><font color='red'><b>Flight Cancelled:</b></font><br>This flight has been cancelled.</html>",
                                "Booking Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        DataManager.selectedFlightData = rawFlight;
                    }
                } else {
                    DataManager.selectedFlightData = null;
                }
                MainDashboardFrame.showCard("PASSENGER_DETAILS");
            }
        });
    }

    // ── Exit dialog ──────────────────────────────────────────────────────────
    private void showExitDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        Frame  frame = (owner instanceof Frame) ? (Frame) owner : null;
        final JDialog dialog = new JDialog(frame, "Exit System", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(owner);
        dialog.setUndecorated(true);

        JPanel pnlDialog = new JPanel(null);
        pnlDialog.setBackground(Color.WHITE);
        pnlDialog.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblMsg = new JLabel("Return to Welcome Screen?", SwingConstants.CENTER);
        lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMsg.setForeground(new Color(30, 45, 75));
        lblMsg.setBounds(0, 50, 400, 30);
        pnlDialog.add(lblMsg);

        ActionButton btnYes = new ActionButton("YES, EXIT", new Color(255, 120, 0), Color.WHITE);
        btnYes.setBounds(50, 110, 140, 45);
        btnYes.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                MainDashboardFrame.showCard("WELCOME");
            }
        });
        pnlDialog.add(btnYes);

        ActionButton btnNo = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnNo.setBounds(210, 110, 140, 45);
        btnNo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        btnNo.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { dialog.dispose(); }
        });
        pnlDialog.add(btnNo);

        dialog.add(pnlDialog);
        dialog.setVisible(true);
    }

    // ── Data loader ──────────────────────────────────────────────────────────
    private void loadFlightRecordsData() {
        tableModel.setRowCount(0);
        String statusFilter = cbFlightStatus.getSelectedItem().toString().toLowerCase();
        String typeFilter   = cbFlightCategory.getSelectedItem().toString().toLowerCase();
        String keyword      = txtSearch.getText().trim().toLowerCase();

        String selectedDateStr = "";
        if (dcDateFilter != null && dcDateFilter.getDate() != null) {
            selectedDateStr = new java.text.SimpleDateFormat("dd-MM-yy").format(dcDateFilter.getDate());
        }

        for (Object[] flight : DataManager.getFlightsDB()) {
            String fId      = flight[0].toString().toLowerCase();
            String fDate    = flight[1].toString().toLowerCase();
            String fOrigin  = flight[2].toString().toLowerCase();
            String fDest    = flight[3].toString().toLowerCase();
            String fStatus  = flight[5].toString().toLowerCase();
            String fType    = flight[7].toString().toLowerCase();
            String fAirline = flight[8].toString().toLowerCase();

            boolean statusMatch  = statusFilter.equals("all statuses") || fStatus.contains(statusFilter.replace(" flights", ""));
            boolean typeMatch    = typeFilter.equals("all flights")     || fType.equalsIgnoreCase(typeFilter);
            boolean dateMatch    = selectedDateStr.isEmpty() || fDate.equalsIgnoreCase(selectedDateStr);

            String corpus = fId + " " + fDest + " " + fDate + " " + fOrigin + " " + fAirline;
            if (fDest.equals("ceb") || fOrigin.equals("ceb")) corpus += " cebu";
            if (fDest.equals("mnl") || fOrigin.equals("mnl")) corpus += " manila";
            if (fDest.equals("dvo") || fOrigin.equals("dvo")) corpus += " davao";
            if (fDest.equals("pps") || fOrigin.equals("pps")) corpus += " puerto princesa";
            if (fDest.equals("mph") || fOrigin.equals("mph")) corpus += " caticlan boracay";
            if (fDest.equals("ilo") || fOrigin.equals("ilo")) corpus += " iloilo";
            if (fDest.equals("nrt") || fOrigin.equals("nrt")) corpus += " tokyo narita japan";
            if (fDest.equals("sin") || fOrigin.equals("sin")) corpus += " singapore";
            if (fDest.equals("lax") || fOrigin.equals("lax")) corpus += " los angeles usa";
            if (fDest.equals("lhr") || fOrigin.equals("lhr")) corpus += " london uk";

            boolean keywordMatch = keyword.isEmpty() || corpus.contains(keyword);

            if (statusMatch && typeMatch && keywordMatch && dateMatch) {
                tableModel.addRow(new Object[]{
                    flight[0], 
                    DataManager.getAirportFullName(flight[2].toString()), 
                    DataManager.getAirportFullName(flight[3].toString()), 
                    flight[1], 
                    flight[4], 
                    flight[10], 
                    flight[5]
                });
            }
        }
        lblSelectedFlight.setText("No flight selected. Please choose a flight from the table.");
    }
}
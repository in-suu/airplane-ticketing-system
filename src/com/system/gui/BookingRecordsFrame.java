package com.system.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;

public class BookingRecordsFrame extends JPanel {

    private static final long serialVersionUID = 1L;

    // ── Core data fields ─────────────────────────────────────────────────────
    private JTextField txtSearch;
    private JTable tblRecords;
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenue;

    // ── Sidebar (instance fields for WindowBuilder) ───────────────────────────
    private SidebarPanel pnlSidebar;
    private NavSidebarButton btnNavDashboard;
    private NavSidebarButton btnNavViewFlights;
    private NavSidebarButton btnNavBookFlights;
    private NavSidebarButton btnNavRecords;
    private NavSidebarButton btnNavExit;

    // ── Main layout panels (instance fields for WindowBuilder) ────────────────
    private JPanel pnlMainBackground;
    private RoundedPanel pnlMain;
    private JLabel lblTitle;
    private JLabel lblSearch;
    private JScrollPane scrollPane;
    private JPanel pnlAnalyticsStrip;
    private ActionButton btnCancelBooking;
    private ActionButton btnViewReceipt;
    private ActionButton btnBackDashboard;

    private int hoveredRow = -1;

    private final Color REFINED_NAVY = new Color(30, 45, 75);
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private final Color TEXT_WHITE = Color.WHITE;
    private final Color SUBTLE_GRAY = new Color(160, 175, 200);

    public BookingRecordsFrame() {
        setPreferredSize(new Dimension(1250, 750)); // Full design-canvas for WindowBuilder
        setBackground(REFINED_NAVY);
        setLayout(new BorderLayout());

        // ── Sidebar ───────────────────────────────────────────────────────────
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
        pnlSidebar.add(btnNavViewFlights);

        btnNavBookFlights = new NavSidebarButton("BOOK FLIGHTS");
        btnNavBookFlights.setBounds(20, 190, 240, 45);
        pnlSidebar.add(btnNavBookFlights);

        btnNavRecords = new NavSidebarButton("RECORDS");
        btnNavRecords.setBounds(20, 245, 240, 45);
        btnNavRecords.setActive(true);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                MainDashboardFrame.showCard("DASHBOARD");
            }
        });
        btnNavViewFlights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainDashboardFrame.showCard("FLIGHT_VIEW");
            }
        });
        btnNavBookFlights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainDashboardFrame.showCard("PASSENGER_DETAILS");
            }
        });
        btnNavRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainDashboardFrame.showCard("RECORDS_VIEW");
            }
        });
        btnNavExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExitDialog();
            }
        });

        // ── Main background ──────────────────────────────────────────────────
        pnlMainBackground = new JPanel(null);
        pnlMainBackground.setBackground(REFINED_NAVY);
        add(pnlMainBackground, BorderLayout.CENTER);

        pnlMain = new RoundedPanel();
        pnlMain.setCornerRadius(25);
        pnlMain.setBackground(SOFT_CARD_NAVY);
        pnlMain.setLayout(null);
        pnlMain.setBounds(40, 40, 890, 670); // Design-time canvas bounds
        pnlMainBackground.add(pnlMain);

        // ── Title ─────────────────────────────────────────────────────────────
        lblTitle = new JLabel("Passenger Booking Records");
        lblTitle.setForeground(TEXT_WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(40, 30, 350, 30);
        pnlMain.add(lblTitle);

        // ── Search bar ────────────────────────────────────────────────────────
        lblSearch = new JLabel("Search Passenger:");
        lblSearch.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblSearch.setForeground(SUBTLE_GRAY);
        lblSearch.setBounds(500, 80, 130, 40); // Initial design-time bounds
        pnlMain.add(lblSearch);

        txtSearch = new RoundedTextField();
        txtSearch.setBounds(620, 80, 230, 40); // Initial design-time bounds
        pnlMain.add(txtSearch);

        // ── Table ─────────────────────────────────────────────────────────────
        String[] columns = { "Ref ID", "Passenger Name", "Flight ID", "Route", "Seat", "Amount", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblRecords = new JTable(tableModel);
        tblRecords.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        tblRecords.setRowHeight(32);
        tblRecords.setGridColor(new Color(235, 240, 245));
        tblRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRecords.setRowSelectionAllowed(true);
        tblRecords.setColumnSelectionAllowed(false);
        tblRecords.setCellSelectionEnabled(false);
        tblRecords.setShowGrid(true);

        JTableHeader header = tblRecords.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(60, 75, 100));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer recordsRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    if (isSelected) {
                        label.setBackground(new Color(210, 225, 245));
                        label.setForeground(REFINED_NAVY);
                    } else if (row == hoveredRow) {
                        label.setBackground(new Color(240, 243, 248));
                        label.setForeground(REFINED_NAVY);
                    } else {
                        label.setBackground(Color.WHITE);
                        label.setForeground(new Color(50, 60, 75));
                    }
                    if (column == 6 && value != null) {
                        String status = value.toString().toUpperCase();
                        if (status.contains("CONFIRMED"))
                            label.setForeground(new Color(20, 140, 60));
                        else if (status.contains("CANCELLED"))
                            label.setForeground(new Color(220, 40, 40));
                    }
                }
                return c;
            }
        };

        for (int i = 0; i < tblRecords.getColumnCount(); i++) {
            tblRecords.getColumnModel().getColumn(i).setCellRenderer(recordsRenderer);
        }

        tblRecords.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tblRecords.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    tblRecords.repaint();
                }
            }
        });
        tblRecords.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                tblRecords.repaint();
            }
        });

        scrollPane = new JScrollPane(tblRecords);
        scrollPane.setBounds(40, 140, 810, 380); // Design-time bounds
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(215, 220, 225), 1));
        pnlMain.add(scrollPane);

        // ── Analytics strip ───────────────────────────────────────────────────
        pnlAnalyticsStrip = new JPanel(null);
        pnlAnalyticsStrip.setBackground(Color.WHITE);
        pnlAnalyticsStrip.setBounds(40, 540, 810, 40); // Design-time bounds
        pnlMain.add(pnlAnalyticsStrip);

        lblTotalRevenue = new JLabel("Total Verified System Gross Revenue Flow: P0.00");
        lblTotalRevenue.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblTotalRevenue.setForeground(new Color(50, 65, 90));
        lblTotalRevenue.setBounds(20, 0, 780, 40);
        pnlAnalyticsStrip.add(lblTotalRevenue);

        // ── Bottom buttons ────────────────────────────────────────────────────
        int btnWidth = (890 - 120) / 3; // = 256
        btnCancelBooking = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnCancelBooking.setBounds(40, 595, btnWidth, 45); // Design-time bounds
        btnCancelBooking.setBorder(new LineBorder(new Color(230, 220, 220), 1));
        pnlMain.add(btnCancelBooking);

        btnViewReceipt = new ActionButton("VIEW RECEIPT", Color.WHITE, REFINED_NAVY);
        btnViewReceipt.setBounds(40 + btnWidth + 20, 595, btnWidth, 45);
        btnViewReceipt.setBorder(new LineBorder(new Color(210, 220, 235), 1));
        pnlMain.add(btnViewReceipt);

        btnBackDashboard = new ActionButton("RETURN TO DASHBOARD", DataManager.SUNSET_ORANGE, Color.WHITE);
        btnBackDashboard.setBounds(40 + (btnWidth * 2) + 40, 595, btnWidth, 45);
        pnlMain.add(btnBackDashboard);

        // ── Dynamic resize listener on pnlMainBackground ──────────────────────
        pnlMainBackground.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = pnlMainBackground.getWidth();
                int h = pnlMainBackground.getHeight();
                if (w <= 0 || h <= 0)
                    return;

                pnlMain.setBounds(40, 40, w - 80, h - 80);

                int pw = pnlMain.getWidth();
                int ph = pnlMain.getHeight();
                if (pw <= 0 || ph <= 0)
                    return;

                lblSearch.setBounds(pw - 390, 80, 130, 40);
                txtSearch.setBounds(pw - 270, 80, 230, 40);
                scrollPane.setBounds(40, 140, pw - 80, ph - 290);
                pnlAnalyticsStrip.setBounds(40, ph - 130, pw - 80, 40);
                lblTotalRevenue.setBounds(20, 0, pw - 40, 40);

                int bw = (pw - 120) / 3;
                btnCancelBooking.setBounds(40, ph - 80, bw, 45);
                btnViewReceipt.setBounds(40 + bw + 20, ph - 80, bw, 45);
                btnBackDashboard.setBounds(40 + (bw * 2) + 40, ph - 80, bw, 45);
            }
        });

        // ── Event listeners ───────────────────────────────────────────────────
        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent ev) {
                loadBookingLedgerData();
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent ev) {
            }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent ev) {
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadBookingLedgerData();
            }
        });

        btnBackDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainDashboardFrame.showCard("DASHBOARD");
            }
        });

        btnCancelBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblRecords.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(BookingRecordsFrame.this, "Please choose a booking record first!",
                            "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String currentStatus = tblRecords.getValueAt(selectedRow, 6).toString();
                if (currentStatus.equals("CANCELLED")) {
                    JOptionPane.showMessageDialog(BookingRecordsFrame.this, "This booking is already CANCELLED.",
                            "Operation Void", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(BookingRecordsFrame.this,
                        "Are you sure you want to cancel this reservation?", "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    tblRecords.setValueAt("CANCELLED", selectedRow, 6);
                    String refId = tblRecords.getValueAt(selectedRow, 0).toString();
                    for (Object[] record : DataManager.bookingRecords) {
                        if (record[0].toString().equals(refId)) {
                            record[6] = "CANCELLED";
                            break;
                        }
                    }
                    calculateRevenueSummary();
                }
            }
        });

        btnViewReceipt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblRecords.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(BookingRecordsFrame.this, "Please select a passenger row first!",
                            "No Record Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String refId = tblRecords.getValueAt(selectedRow, 0).toString();
                String name = tblRecords.getValueAt(selectedRow, 1).toString();
                String flightId = tblRecords.getValueAt(selectedRow, 2).toString();
                String route = tblRecords.getValueAt(selectedRow, 3).toString();
                String seat = tblRecords.getValueAt(selectedRow, 4).toString();
                String price = tblRecords.getValueAt(selectedRow, 5).toString();
                String status = tblRecords.getValueAt(selectedRow, 6).toString();
                openDigitalReceiptModal(refId, name, flightId, route, seat, price, status);
            }
        });
    }

    // ── Exit dialog ───────────────────────────────────────────────────────────
    private void showExitDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        Frame frame = (owner instanceof Frame) ? (Frame) owner : null;
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
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                MainDashboardFrame.showCard("WELCOME");
            }
        });
        pnlDialog.add(btnYes);

        ActionButton btnNo = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnNo.setBounds(210, 110, 140, 45);
        btnNo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        btnNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        pnlDialog.add(btnNo);

        dialog.add(pnlDialog);
        dialog.setVisible(true);
    }

    // ── Receipt modal ─────────────────────────────────────────────────────────
    private void openDigitalReceiptModal(String ref, String name, String flight, String route, String seat,
            String price, String status) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Biyaheng Langit - Official Ticket Stub", true);
        dialog.setSize(380, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel pnlReceipt = new JPanel(null);
        pnlReceipt.setBackground(new Color(245, 247, 250));

        JLabel lblHeader = new JLabel("BIYAHENG LANGIT", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(DataManager.SUNSET_ORANGE);
        lblHeader.setBounds(30, 35, 320, 25);
        pnlReceipt.add(lblHeader);

        JLabel lblSub = new JLabel("Official Flight Passenger Receipt", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
        lblSub.setForeground(new Color(120, 135, 155));
        lblSub.setBounds(30, 60, 320, 15);
        pnlReceipt.add(lblSub);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 90, 300, 2);
        pnlReceipt.add(sep);

        int startY = 110;
        int rowHeight = 32;
        String[][] dataFields = {
                { "Transaction Ref:", ref },
                { "Passenger Name:", name },
                { "Flight Segment ID:", flight },
                { "Itinerary Route:", route },
                { "Assigned Seat Row:", seat },
                { "Total Fare Paid:", "PHP " + price + ".00" },
                { "Ledger Status:", status }
        };

        for (String[] field : dataFields) {
            JLabel lblKey = new JLabel(field[0]);
            lblKey.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
            lblKey.setForeground(new Color(100, 115, 140));
            lblKey.setBounds(45, startY, 130, 20);
            pnlReceipt.add(lblKey);

            JLabel lblVal = new JLabel(field[1]);
            lblVal.setFont(new Font("Segoe UI", Font.BOLD, 12));
            if (field[0].contains("Status")) {
                lblVal.setForeground(field[1].equals("CONFIRMED") ? new Color(20, 140, 60) : Color.RED);
            } else {
                lblVal.setForeground(REFINED_NAVY);
            }
            lblVal.setBounds(175, startY, 160, 20);
            pnlReceipt.add(lblVal);
            startY += rowHeight;
        }

        JLabel lblFoot = new JLabel("Thank you for flying with us! Have a safe trip.", SwingConstants.CENTER);
        lblFoot.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFoot.setForeground(new Color(140, 150, 170));
        lblFoot.setBounds(30, 365, 320, 20);
        pnlReceipt.add(lblFoot);

        JButton btnClose = new JButton("CLOSE RECEIPT");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(REFINED_NAVY);
        btnClose.setBounds(110, 405, 160, 32);
        btnClose.setFocusable(false);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        pnlReceipt.add(btnClose);

        dialog.getContentPane().add(pnlReceipt);
        dialog.setVisible(true);
    }

    // ── Data loading ──────────────────────────────────────────────────────────
    private void loadBookingLedgerData() {
        tableModel.setRowCount(0);
        String keyword = txtSearch.getText().trim().toLowerCase();

        for (Object[] row : DataManager.getMockRecordsDB()) {
            String passenger = row[1].toString().toLowerCase();
            String txnID = row[0].toString().toLowerCase();
            String flightId = row[2].toString().toLowerCase();
            String route = row[3].toString().toLowerCase();

            String flightDate = "";
            for (Object[] f : DataManager.getMockFlightsDB()) {
                if (f[0].toString().equalsIgnoreCase(flightId)) {
                    flightDate = f[1].toString().toLowerCase();
                    break;
                }
            }

            String corpus = passenger + " " + txnID + " " + flightId + " " + route + " " + flightDate;
            if (route.contains("ceb"))
                corpus += " cebu";
            if (route.contains("mnl"))
                corpus += " manila";
            if (route.contains("dvo"))
                corpus += " davao";
            if (route.contains("pps"))
                corpus += " puerto princesa";
            if (route.contains("mph"))
                corpus += " caticlan boracay";
            if (route.contains("ilo"))
                corpus += " iloilo";
            if (route.contains("nrt"))
                corpus += " tokyo narita japan";
            if (route.contains("sin"))
                corpus += " singapore";
            if (route.contains("lax"))
                corpus += " los angeles usa";
            if (route.contains("lhr"))
                corpus += " london uk";

            if (keyword.isEmpty() || corpus.contains(keyword)) {
                tableModel.addRow(row);
            }
        }
        calculateRevenueSummary();
    }

    private void calculateRevenueSummary() {
        int total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 6).toString().equals("CONFIRMED")) {
                try {
                    total += Integer.parseInt(tableModel.getValueAt(i, 5).toString());
                } catch (Exception ex) {
                }
            }
        }
        lblTotalRevenue.setText("Total Verified System Gross Revenue Flow: PHP " + String.format("%,d", total) + ".00");
    }
}
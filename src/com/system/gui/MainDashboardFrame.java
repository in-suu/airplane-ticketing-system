package com.system.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainDashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // ── Static card-router (set during initComponents) ──────────────────────
    private static CardLayout cardLayout;
    private static JPanel     cardPanel;

    // ── Design tokens ────────────────────────────────────────────────────────
    private final Color REFINED_NAVY   = new Color(30, 45, 75);
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private final Color SUBTLE_GRAY    = new Color(160, 175, 200);

    // ── Instance fields – all components WindowBuilder needs to see ──────────
    private JPanel          pnlDashboardShell;
    private SidebarPanel    pnlSidebar;
    private NavSidebarButton btnNavDashboard;
    private NavSidebarButton btnNavViewFlights;
    private NavSidebarButton btnNavBookFlights;
    private NavSidebarButton btnNavRecords;
    private NavSidebarButton btnNavExit;

    private JPanel          pnlMain;
    private JLabel          lblDashboardTitle;
    private JLabel          lblCurrentDate;
    private ModernStatCard  statActiveFlights;
    private ModernStatCard  statTodayBookings;
    private ModernStatCard  statSystemStatus;
    // Quick-Search action card (inlined for WindowBuilder Design tab)
    private JPanel        pnlSearchCard;
    private JLabel        lblSearchTitle;
    private JLabel        lblSearchDesc;
    private RoundedButton btnSearchNow;

    // Passenger-Records action card (inlined for WindowBuilder Design tab)
    private JPanel        pnlRecordsCard;
    private JLabel        lblRecordsTitle;
    private JLabel        lblRecordsDesc;
    private RoundedButton btnViewLogs;

    // ── Entry point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainDashboardFrame frame = new MainDashboardFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ── Static card switcher (called by child panels) ────────────────────────
    public static void showCard(String cardName) {
        if (cardLayout != null && cardPanel != null) {
            cardLayout.show(cardPanel, cardName);
        }
    }

    // ── Constructor ──────────────────────────────────────────────────────────
    public MainDashboardFrame() {
        initComponents();
    }

    // ── initComponents – WindowBuilder parses this method ────────────────────
    private void initComponents() {
        setTitle("Biyaheng Langit - Enterprise Flight Management Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 750));
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Root card panel ────────────────────────────────────────────────────
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        setContentPane(cardPanel);

        // ════════════════════════════════════════════════════════════════════
        // WELCOME CARD
        // ════════════════════════════════════════════════════════════════════
        cardPanel.add(new WelcomeFrame(), "WELCOME");

        // ════════════════════════════════════════════════════════════════════
        // DASHBOARD CARD  (fully inlined so WindowBuilder sees every widget)
        // ════════════════════════════════════════════════════════════════════
        pnlDashboardShell = new JPanel(new BorderLayout());
        pnlDashboardShell.setBackground(REFINED_NAVY);
        cardPanel.add(pnlDashboardShell, "DASHBOARD");

        // ── Sidebar ──────────────────────────────────────────────────────────
        pnlSidebar = new SidebarPanel();
        pnlSidebar.setBackgroundImagePath("/com/system/images/Untitled design.jpg");
        pnlSidebar.setPreferredSize(new Dimension(280, 0));
        pnlSidebar.setLayout(null);
        pnlDashboardShell.add(pnlSidebar, BorderLayout.WEST);

        btnNavDashboard   = new NavSidebarButton("DASHBOARD");
        btnNavDashboard.setBounds(20, 80, 240, 45);
        btnNavDashboard.setActive(true);
        pnlSidebar.add(btnNavDashboard);

        btnNavViewFlights = new NavSidebarButton("VIEW FLIGHTS");
        btnNavViewFlights.setBounds(20, 135, 240, 45);
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

        // Sidebar resize: anchor EXIT to bottom
        pnlSidebar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                btnNavExit.setBounds(20, pnlSidebar.getHeight() - 80, 240, 45);
            }
        });

        // Sidebar actions
        btnNavDashboard.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("DASHBOARD"); }
        });
        btnNavViewFlights.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("FLIGHT_VIEW"); }
        });
        btnNavBookFlights.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("PASSENGER_DETAILS"); }
        });
        btnNavRecords.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("RECORDS_VIEW"); }
        });
        btnNavExit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showExitDialog(); }
        });

        // ── Main content area ────────────────────────────────────────────────
        pnlMain = new JPanel(null);
        pnlMain.setBackground(REFINED_NAVY);
        pnlDashboardShell.add(pnlMain, BorderLayout.CENTER);

        lblDashboardTitle = new JLabel("Dashboard Overview");
        lblDashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblDashboardTitle.setForeground(Color.WHITE);
        lblDashboardTitle.setBounds(40, 30, 400, 35);
        pnlMain.add(lblDashboardTitle);

        lblCurrentDate = new JLabel(new SimpleDateFormat("EEEE, MMM dd, yyyy").format(new Date()));
        lblCurrentDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblCurrentDate.setForeground(SUBTLE_GRAY);
        lblCurrentDate.setBounds(40, 65, 400, 20);
        pnlMain.add(lblCurrentDate);

        // Stat cards
        statActiveFlights = new ModernStatCard("ACTIVE FLIGHTS", "24");
        statActiveFlights.setBounds(40, 110, 286, 110);
        pnlMain.add(statActiveFlights);

        statTodayBookings = new ModernStatCard("TODAY'S BOOKINGS", "156");
        statTodayBookings.setBounds(341, 110, 286, 110);
        pnlMain.add(statTodayBookings);

        statSystemStatus = new ModernStatCard("SYSTEM STATUS", "NORMAL");
        statSystemStatus.setBounds(642, 110, 286, 110);
        pnlMain.add(statSystemStatus);

        // ── Quick Search card ─────────────────────────────────────────────────
        pnlSearchCard = new JPanel(null);
        pnlSearchCard.setBackground(Color.WHITE);
        pnlSearchCard.setBounds(40, 250, 890, 105);
        pnlMain.add(pnlSearchCard);

        lblSearchTitle = new JLabel("Quick Flight Search");
        lblSearchTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblSearchTitle.setForeground(new Color(30, 45, 75));
        lblSearchTitle.setBounds(25, 22, 300, 25);
        pnlSearchCard.add(lblSearchTitle);

        lblSearchDesc = new JLabel("Instantly view all available domestic and international routes.");
        lblSearchDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSearchDesc.setForeground(new Color(100, 110, 130));
        lblSearchDesc.setBounds(25, 48, 700, 20);
        pnlSearchCard.add(lblSearchDesc);

        btnSearchNow = new RoundedButton("SEARCH NOW");
        btnSearchNow.setRadius(12);
        btnSearchNow.setDefaultColor(DataManager.SUNSET_ORANGE);
        btnSearchNow.setHoverColor(new Color(255, 140, 30));
        btnSearchNow.setPressedColor(new Color(200, 90, 0));
        btnSearchNow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSearchNow.setForeground(Color.WHITE);
        btnSearchNow.setBounds(740, 30, 130, 45);
        pnlSearchCard.add(btnSearchNow);

        btnSearchNow.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("FLIGHT_VIEW"); }
        });

        // ── Passenger Records card ────────────────────────────────────────────
        pnlRecordsCard = new JPanel(null);
        pnlRecordsCard.setBackground(Color.WHITE);
        pnlRecordsCard.setBounds(40, 380, 890, 105);
        pnlMain.add(pnlRecordsCard);

        lblRecordsTitle = new JLabel("Passenger Records");
        lblRecordsTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblRecordsTitle.setForeground(new Color(30, 45, 75));
        lblRecordsTitle.setBounds(25, 22, 300, 25);
        pnlRecordsCard.add(lblRecordsTitle);

        lblRecordsDesc = new JLabel("Manage and review all confirmed flight reservations.");
        lblRecordsDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRecordsDesc.setForeground(new Color(100, 110, 130));
        lblRecordsDesc.setBounds(25, 48, 700, 20);
        pnlRecordsCard.add(lblRecordsDesc);

        btnViewLogs = new RoundedButton("VIEW LOGS");
        btnViewLogs.setRadius(12);
        btnViewLogs.setDefaultColor(DataManager.SUNSET_ORANGE);
        btnViewLogs.setHoverColor(new Color(255, 140, 30));
        btnViewLogs.setPressedColor(new Color(200, 90, 0));
        btnViewLogs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnViewLogs.setForeground(Color.WHITE);
        btnViewLogs.setBounds(740, 30, 130, 45);
        pnlRecordsCard.add(btnViewLogs);

        btnViewLogs.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { showCard("RECORDS_VIEW"); }
        });

        // Responsive resize – re-positions all cards proportionally
        pnlMain.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = pnlMain.getWidth();
                int cardW = (w - 110) / 3;
                statActiveFlights.setBounds(40,                     110, cardW, 110);
                statTodayBookings.setBounds(40 + cardW + 15,        110, cardW, 110);
                statSystemStatus .setBounds(40 + (cardW * 2) + 30, 110, cardW, 110);
                pnlSearchCard .setBounds(40, 250, w - 80, 105);
                pnlRecordsCard.setBounds(40, 380, w - 80, 105);
                btnSearchNow.setBounds(pnlSearchCard.getWidth()  - 150, 30, 130, 45);
                btnViewLogs .setBounds(pnlRecordsCard.getWidth() - 150, 30, 130, 45);
                lblSearchDesc .setBounds(25, 48, pnlSearchCard.getWidth()  - 180, 20);
                lblRecordsDesc.setBounds(25, 48, pnlRecordsCard.getWidth() - 180, 20);
            }
        });

        // ════════════════════════════════════════════════════════════════════
        // OTHER NAVIGATION CARDS
        // ════════════════════════════════════════════════════════════════════
        cardPanel.add(new FlightViewFrame(),       "FLIGHT_VIEW");
        cardPanel.add(new PassengerDetailsFrame(), "PASSENGER_DETAILS");
        cardPanel.add(new BookingRecordsFrame(),   "RECORDS_VIEW");

        // Start on Welcome screen
        showCard("WELCOME");
    }

    // ── Exit confirmation dialog ─────────────────────────────────────────────
    private void showExitDialog() {
        final JDialog dialog = new JDialog(this, "Exit System", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
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
                showCard("WELCOME");
            }
        });
        pnlDialog.add(btnYes);

        ActionButton btnNo = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnNo.setBounds(210, 110, 140, 45);
        btnNo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        btnNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { dialog.dispose(); }
        });
        pnlDialog.add(btnNo);

        dialog.add(pnlDialog);
        dialog.setVisible(true);
    }
}
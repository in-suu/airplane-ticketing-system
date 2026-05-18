package com.system.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class PassengerDetailsFrame extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane, pnlForm;
    private JTextField txtName, txtAge, txtContact, txtPassport, txtPassengerCountInput, txtDiscountID;
    private JComboBox<String> cbClassType, cbBaggage, cbDiscount, cbNationality, cbTripType, cbSpecialCargo, cbFlightType;
    private JCheckBox chkInsurance;
    private JLabel lblTitle, lblSummary, lblSeatNo, lblFlightStatusCard, lblPassport, lblSpecialCargo, lblDiscount, lblDiscountID, lblReturnSeatNo;
    private JButton btnConfirm, btnSeatMap, btnVerify, btnChangeFlight, btnReturnSeatMap;
    private ActionButton btnCancel;
    private RoundedTextField txtFlightSearch;
    private JDateChooser dateDeparture, dateReturn;
    private JLabel lblDeparture, lblReturn;

    // ── Sidebar & panels (instance fields for WindowBuilder) ─────────────────
    private SidebarPanel    pnlSidebar;
    private JPanel          pnlMainBackground;
    private RoundedPanel    pnlMain;
    private NavSidebarButton btnNavDashboard;
    private NavSidebarButton btnNavViewFlights;
    private NavSidebarButton btnNavBookFlights;
    private NavSidebarButton btnNavRecords;
    private NavSidebarButton btnNavExit;

    private boolean isFlightVerified = false;
    private Object[] targetFlightData = null;

    private int currentPassenger = 1;
    private int totalPassengers = 1; 
    private String selectedSeat = "None";
    private String selectedReturnSeat = "None";
    
    private List<String> billingRecords = new ArrayList<>();

    private final Color REFINED_NAVY = new Color(30, 45, 75); 
    private final Color SOFT_CARD_NAVY = new Color(42, 62, 102);
    private final Color TEXT_WHITE = Color.WHITE;
    private final Color SUBTLE_GRAY = new Color(160, 175, 200);

    public PassengerDetailsFrame() {
        setPreferredSize(new Dimension(1250, 750)); // Full design-canvas for WindowBuilder
        contentPane = this;
        contentPane.setBackground(REFINED_NAVY);
        contentPane.setLayout(new BorderLayout());

        // ── Sidebar ──────────────────────────────────────────────────────────
        pnlSidebar = new SidebarPanel();
        pnlSidebar.setBackgroundImagePath("/com/system/images/Untitled design.jpg");
        pnlSidebar.setPreferredSize(new Dimension(280, 0));
        pnlSidebar.setLayout(null);
        contentPane.add(pnlSidebar, BorderLayout.WEST);

        btnNavDashboard = new NavSidebarButton("DASHBOARD");
        btnNavDashboard.setBounds(20, 80, 240, 45);
        pnlSidebar.add(btnNavDashboard);

        btnNavViewFlights = new NavSidebarButton("VIEW FLIGHTS");
        btnNavViewFlights.setBounds(20, 135, 240, 45);
        pnlSidebar.add(btnNavViewFlights);

        btnNavBookFlights = new NavSidebarButton("BOOK FLIGHTS");
        btnNavBookFlights.setBounds(20, 190, 240, 45);
        btnNavBookFlights.setActive(true);
        pnlSidebar.add(btnNavBookFlights);

        btnNavRecords = new NavSidebarButton("RECORDS");
        btnNavRecords.setBounds(20, 245, 240, 45);
        pnlSidebar.add(btnNavRecords);

        btnNavExit = new NavSidebarButton("EXIT");
        btnNavExit.setBounds(20, 460, 240, 45);
        pnlSidebar.add(btnNavExit);

        pnlSidebar.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
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
            @Override public void actionPerformed(ActionEvent e) { showExitDialogPassenger(); }
        });

        // ── Main content area ────────────────────────────────────────────────
        pnlMainBackground = new JPanel(null);
        pnlMainBackground.setBackground(REFINED_NAVY);
        contentPane.add(pnlMainBackground, BorderLayout.CENTER);

        pnlMain = new RoundedPanel();
        pnlMain.setCornerRadius(25);
        pnlMain.setBackground(SOFT_CARD_NAVY);
        pnlMain.setLayout(null);
        // Initial design-time bounds (updated at runtime by componentResized)
        pnlMain.setBounds(40, 40, 890, 670);
        pnlMainBackground.add(pnlMain);

        lblTitle = new JLabel("Passenger Registration & Booking");
        lblTitle.setForeground(TEXT_WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(40, 30, 550, 30);
        pnlMain.add(lblTitle);

        lblSummary = new JLabel("Flight: None Selected | Base Price: Awaiting Verification");
        lblSummary.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 13));
        lblSummary.setForeground(SUBTLE_GRAY);
        lblSummary.setBounds(25, 55, 550, 20);
        pnlMain.add(lblSummary);

        // --- UNIFIED MAIN FORM CONTENT PANEL ---
        pnlForm = new JPanel(null);
        pnlForm.setBounds(0, 80, 850, 570);
        pnlForm.setOpaque(false);
        pnlMain.add(pnlForm);

        // --- DYNAMIC RESIZING ALIGNMENT LISTENER ---
        pnlMainBackground.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = pnlMainBackground.getWidth();
                int h = pnlMainBackground.getHeight();
                
                pnlMain.setBounds(40, 40, w - 80, h - 80);
                
                int pw = pnlMain.getWidth();
                int formX = Math.max(0, (pw - 850) / 2); // Center the form block
                
                pnlForm.setBounds(formX, 80, 850, 570);
                lblTitle.setBounds(formX + 40, 30, 550, 30);
                lblSummary.setBounds(formX + 25, 55, 550, 20);
            }
        });

        JLabel lblSearchTitle = new JLabel("Search Flight (Place / ID) *:");
        styleFormLabel(lblSearchTitle, 40, 5);
        pnlForm.add(lblSearchTitle);

        txtFlightSearch = new RoundedTextField();
        txtFlightSearch.setBounds(40, 30, 120, 40);
        pnlForm.add(txtFlightSearch);

        JPopupMenu suggestPopup = new JPopupMenu();
        suggestPopup.setFocusable(false);
        txtFlightSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateSuggestions() {
                SwingUtilities.invokeLater(() -> {
                    if (!txtFlightSearch.isEnabled() || !txtFlightSearch.isFocusOwner()) return;
                    String query = txtFlightSearch.getText().trim().toLowerCase();
                    suggestPopup.removeAll();
                    if (query.isEmpty()) {
                        suggestPopup.setVisible(false);
                        return;
                    }
                    Object[][] db = DataManager.getMockFlightsDB();
                    int matchCount = 0;
                    for (Object[] row : db) {
                        String id = row[0].toString().toLowerCase();
                        String date = row[1].toString().toLowerCase();
                        String origin = row[2].toString().toLowerCase();
                        String dest = row[3].toString().toLowerCase();
                        
                        String searchCorpus = id + " " + dest + " " + date + " " + origin;
                        if (dest.equals("ceb") || origin.equals("ceb")) searchCorpus += " cebu";
                        if (dest.equals("mnl") || origin.equals("mnl")) searchCorpus += " manila";
                        if (dest.equals("dvo") || origin.equals("dvo")) searchCorpus += " davao";
                        if (dest.equals("pps") || origin.equals("pps")) searchCorpus += " puerto princesa";
                        if (dest.equals("mph") || origin.equals("mph")) searchCorpus += " caticlan boracay";
                        if (dest.equals("ilo") || origin.equals("ilo")) searchCorpus += " iloilo";
                        if (dest.equals("nrt") || origin.equals("nrt")) searchCorpus += " tokyo narita japan";
                        if (dest.equals("sin") || origin.equals("sin")) searchCorpus += " singapore";
                        if (dest.equals("lax") || origin.equals("lax")) searchCorpus += " los angeles usa";
                        if (dest.equals("lhr") || origin.equals("lhr")) searchCorpus += " london uk";

                        if (searchCorpus.contains(query)) {
                            JMenuItem item = new JMenuItem(row[0] + " | " + row[2] + " \u2192 " + row[3]);
                            item.addActionListener(e -> {
                                txtFlightSearch.setText(row[0].toString());
                                suggestPopup.setVisible(false);
                                if (btnVerify != null) btnVerify.doClick(); 
                            });
                            suggestPopup.add(item);
                            matchCount++;
                        }
                    }
                    if (matchCount > 0) {
                        if (!suggestPopup.isVisible()) {
                            suggestPopup.show(txtFlightSearch, 0, txtFlightSearch.getHeight());
                        } else {
                            suggestPopup.pack(); 
                        }
                        txtFlightSearch.requestFocus();
                    } else {
                        suggestPopup.setVisible(false);
                    }
                });
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSuggestions(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSuggestions(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSuggestions(); }
        });



        JLabel lblFlightType = new JLabel("Flight Type *:");
        styleFormLabel(lblFlightType, 170, 5);
        pnlForm.add(lblFlightType);

        cbFlightType = new JComboBox<>(new String[]{"Domestic", "International"});
        cbFlightType.setBounds(170, 30, 110, 40);
        cbFlightType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFlightType.setBackground(Color.WHITE);
        cbFlightType.setFocusable(false);
        pnlForm.add(cbFlightType);

        cbFlightType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isIntl = cbFlightType.getSelectedItem().toString().equals("International");
                if (lblPassport != null) lblPassport.setVisible(isIntl);
                if (txtPassport != null) txtPassport.setVisible(isIntl);
                
                if (isIntl) {
                    if (lblSpecialCargo != null) lblSpecialCargo.setLocation(40, 310);
                    if (cbSpecialCargo != null) cbSpecialCargo.setLocation(40, 330);
                    if (lblDiscount != null) lblDiscount.setLocation(40, 380);
                    if (cbDiscount != null) cbDiscount.setLocation(40, 400);
                    if (lblDiscountID != null) lblDiscountID.setLocation(40, 450);
                    if (txtDiscountID != null) txtDiscountID.setLocation(40, 470);
                } else {
                    if (lblSpecialCargo != null) lblSpecialCargo.setLocation(40, 240);
                    if (cbSpecialCargo != null) cbSpecialCargo.setLocation(40, 260);
                    if (lblDiscount != null) lblDiscount.setLocation(40, 310);
                    if (cbDiscount != null) cbDiscount.setLocation(40, 330);
                    if (lblDiscountID != null) lblDiscountID.setLocation(40, 380);
                    if (txtDiscountID != null) txtDiscountID.setLocation(40, 400);
                }
                
                if (cbDiscount != null) {
                    Object currentSelection = cbDiscount.getSelectedItem();
                    cbDiscount.removeAllItems();
                    cbDiscount.addItem("NONE");
                    cbDiscount.addItem("SENIOR CITIZEN (20%)");
                    cbDiscount.addItem("PWD (20%)");
                    if (!isIntl) {
                        cbDiscount.addItem("STUDENT (20%)");
                    }
                    if (currentSelection != null) {
                        String sel = currentSelection.toString();
                        if (isIntl && sel.startsWith("STUDENT")) {
                            cbDiscount.setSelectedItem("NONE");
                        } else {
                            if (sel.startsWith("STUDENT")) sel = "STUDENT (20%)";
                            cbDiscount.setSelectedItem(sel);
                        }
                    }
                }
            }
        });

        JLabel lblCountTitle = new JLabel("Pax Count *:");
        styleFormLabel(lblCountTitle, 290, 5);
        pnlForm.add(lblCountTitle);

        txtPassengerCountInput = new RoundedTextField();
        txtPassengerCountInput.setBounds(290, 30, 40, 40);
        txtPassengerCountInput.setText("1");
        pnlForm.add(txtPassengerCountInput);

        btnVerify = new ActionButton("VERIFY", Color.WHITE, REFINED_NAVY);
        btnVerify.setBounds(340, 30, 65, 40);
        btnVerify.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        pnlForm.add(btnVerify);

        btnChangeFlight = new ActionButton("CHANGE", Color.WHITE, Color.DARK_GRAY);
        btnChangeFlight.setBounds(415, 30, 65, 40);
        btnChangeFlight.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        btnChangeFlight.setEnabled(false);
        pnlForm.add(btnChangeFlight);

        lblFlightStatusCard = new JLabel("Awaiting localized validation input segment...", SwingConstants.CENTER);
        lblFlightStatusCard.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblFlightStatusCard.setBounds(490, 30, 320, 40);
        lblFlightStatusCard.setOpaque(true);
        lblFlightStatusCard.setBackground(SOFT_CARD_NAVY);
        lblFlightStatusCard.setForeground(SUBTLE_GRAY);
        pnlForm.add(lblFlightStatusCard);

        JSeparator topSep = new JSeparator();
        topSep.setBounds(40, 90, 770, 2);
        pnlForm.add(topSep);

        // --- ROW 1 (100) ---
        JLabel lbl1 = new JLabel("Full Name *:");
        styleFormLabel(lbl1, 40, 100);
        pnlForm.add(lbl1);
        txtName = new RoundedTextField();
        txtName.setBounds(40, 120, 365, 40);
        pnlForm.add(txtName);

        JLabel lblAge = new JLabel("Age *:");
        styleFormLabel(lblAge, 445, 100);
        pnlForm.add(lblAge);
        txtAge = new RoundedTextField();
        txtAge.setBounds(445, 120, 120, 40);
        pnlForm.add(txtAge);

        JLabel lblNationality = new JLabel("Nationality *:");
        styleFormLabel(lblNationality, 585, 100);
        pnlForm.add(lblNationality);
        cbNationality = new JComboBox<>(new String[]{"Filipino", "Foreigner"});
        cbNationality.setBounds(585, 120, 225, 40);
        cbNationality.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbNationality.setBackground(Color.WHITE);
        cbNationality.setFocusable(false);
        pnlForm.add(cbNationality);



        // --- ROW 2 (170) ---
        JLabel lblContact = new JLabel("Contact Number *:");
        styleFormLabel(lblContact, 40, 170);
        pnlForm.add(lblContact);
        txtContact = new RoundedTextField();
        txtContact.setBounds(40, 190, 365, 40);
        pnlForm.add(txtContact);

        JLabel lblTrip = new JLabel("Trip Type *:");
        styleFormLabel(lblTrip, 445, 170);
        pnlForm.add(lblTrip);
        cbTripType = new JComboBox<>(new String[]{"One-Way", "Round Trip"});
        cbTripType.setBounds(445, 190, 110, 40);
        cbTripType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbTripType.setBackground(Color.WHITE);
        cbTripType.setFocusable(false);
        pnlForm.add(cbTripType);

        lblDeparture = new JLabel("Departure Date *:");
        styleFormLabel(lblDeparture, 565, 170);
        lblDeparture.setVisible(true);
        pnlForm.add(lblDeparture);

        dateDeparture = new JDateChooser();
        dateDeparture.setBounds(565, 190, 245, 40);
        dateDeparture.setVisible(true);
        pnlForm.add(dateDeparture);

        lblReturn = new JLabel("Return Date *:");
        styleFormLabel(lblReturn, 690, 170);
        lblReturn.setVisible(false);
        pnlForm.add(lblReturn);

        dateReturn = new JDateChooser();
        dateReturn.setBounds(690, 190, 120, 40);
        dateReturn.setVisible(false);
        pnlForm.add(dateReturn);

        cbTripType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isRoundTrip = cbTripType.getSelectedItem().toString().equals("Round Trip");
                if (isRoundTrip) {
                    dateDeparture.setBounds(565, 190, 115, 40);
                    if (lblReturn != null) lblReturn.setVisible(true);
                    if (dateReturn != null) dateReturn.setVisible(true);
                    if (btnSeatMap != null) {
                        btnSeatMap.setText("DEP SEAT");
                        btnSeatMap.setBounds(445, 400, 90, 40);
                        lblSeatNo.setBounds(540, 400, 85, 40);
                        lblSeatNo.setText("D: " + (selectedSeat.equals("None") ? "None" : selectedSeat));
                    }
                    if (btnReturnSeatMap != null) btnReturnSeatMap.setVisible(true);
                    if (lblReturnSeatNo != null) {
                        lblReturnSeatNo.setVisible(true);
                        lblReturnSeatNo.setText("R: " + (selectedReturnSeat.equals("None") ? "None" : selectedReturnSeat));
                    }
                } else {
                    dateDeparture.setBounds(565, 190, 245, 40);
                    if (lblReturn != null) lblReturn.setVisible(false);
                    if (dateReturn != null) dateReturn.setVisible(false);
                    if (btnSeatMap != null) {
                        btnSeatMap.setText("SELECT SEAT");
                        btnSeatMap.setBounds(445, 400, 180, 40);
                        lblSeatNo.setBounds(645, 400, 165, 40);
                        lblSeatNo.setText("Seat: " + (selectedSeat.equals("None") ? "None" : selectedSeat));
                    }
                    if (btnReturnSeatMap != null) btnReturnSeatMap.setVisible(false);
                    if (lblReturnSeatNo != null) lblReturnSeatNo.setVisible(false);
                }
            }
        });

        // --- ROW 3 (240) ---
        lblPassport = new JLabel("Passport Number:");
        styleFormLabel(lblPassport, 40, 240);
        lblPassport.setVisible(false);
        pnlForm.add(lblPassport);
        txtPassport = new RoundedTextField();
        txtPassport.setBounds(40, 260, 365, 40);
        txtPassport.setVisible(false);
        pnlForm.add(txtPassport);

        JLabel lblClass = new JLabel("Cabin Class Type *:");
        styleFormLabel(lblClass, 445, 240);
        pnlForm.add(lblClass);
        cbClassType = new JComboBox<>(new String[]{"Economy Class [Base]", "Business Class (+P4,500)"});
        cbClassType.setBounds(445, 260, 365, 40);
        cbClassType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbClassType.setBackground(Color.WHITE);
        cbClassType.setFocusable(false);
        pnlForm.add(cbClassType);

        cbClassType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSeat = "None";
                lblSeatNo.setText("Seat: None");
            }
        });

        // --- ROW 4 (310) ---
        lblSpecialCargo = new JLabel("Special Cargo Declaration *:");
        styleFormLabel(lblSpecialCargo, 40, 240);
        pnlForm.add(lblSpecialCargo);
        cbSpecialCargo = new JComboBox<>(new String[]{
            "None [No Special Cargo]",
            "Human Remains / Coffin (+P15,000)",
            "Oversized Baggage (+P3,500)",
            "Fragile / Valuable Items (+P1,200)",
            "Live Animals / Pets (+P2,500)"
        });
        cbSpecialCargo.setBounds(40, 260, 365, 40);
        cbSpecialCargo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbSpecialCargo.setBackground(Color.WHITE);
        cbSpecialCargo.setFocusable(false);
        pnlForm.add(cbSpecialCargo);

        JLabel lblBaggage = new JLabel("Additional Baggage Allowances *:");
        lblBaggage.setBounds(445, 310, 250, 20);
        lblBaggage.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBaggage.setForeground(new Color(180, 195, 220));
        pnlForm.add(lblBaggage);

        cbBaggage = new JComboBox<>(new String[]{
            "Hand Carry 7kg Only [FREE]",
            "Upgrade +20kg Check-in (+P550)",
            "Upgrade +32kg Check-in (+P950)",
            "Upgrade +50kg Check-in (+P1,400)"
        });
        cbBaggage.setBounds(445, 330, 365, 40);
        cbBaggage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbBaggage.setBackground(Color.WHITE);
        cbBaggage.setFocusable(false);
        pnlForm.add(cbBaggage);

        lblDiscount = new JLabel("Applicable Discount *:");
        styleFormLabel(lblDiscount, 40, 310);
        pnlForm.add(lblDiscount);

        cbDiscount = new JComboBox<>(new String[]{"NONE", "SENIOR CITIZEN (20%)", "PWD (20%)", "STUDENT (20%)"});
        cbDiscount.setBounds(40, 330, 365, 40);
        cbDiscount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbDiscount.setBackground(Color.WHITE);
        cbDiscount.setFocusable(false);
        pnlForm.add(cbDiscount);

        lblDiscountID = new JLabel("Discount ID Number (PWD/LRN/Senior) *:");
        styleFormLabel(lblDiscountID, 40, 380);
        lblDiscountID.setVisible(false);
        pnlForm.add(lblDiscountID);

        txtDiscountID = new RoundedTextField();
        txtDiscountID.setBounds(40, 400, 365, 40);
        txtDiscountID.setVisible(false);
        pnlForm.add(txtDiscountID);

        cbDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbDiscount.getSelectedItem() != null) {
                    boolean hasDiscount = !cbDiscount.getSelectedItem().toString().equals("NONE");
                    if (lblDiscountID != null) lblDiscountID.setVisible(hasDiscount);
                    if (txtDiscountID != null) txtDiscountID.setVisible(hasDiscount);
                }
            }
        });

        JLabel lblSeatSelection = new JLabel("Aircraft Seat Assignment *:");
        styleFormLabel(lblSeatSelection, 445, 380);
        pnlForm.add(lblSeatSelection);
        
        btnSeatMap = new ActionButton("SELECT SEAT", Color.WHITE, REFINED_NAVY);
        btnSeatMap.setBounds(445, 400, 180, 40);
        btnSeatMap.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        pnlForm.add(btnSeatMap);

        lblSeatNo = new JLabel("Seat: None");
        lblSeatNo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeatNo.setForeground(DataManager.SUNSET_ORANGE);
        lblSeatNo.setBounds(645, 400, 165, 40);
        pnlForm.add(lblSeatNo);
        
        btnReturnSeatMap = new ActionButton("RET SEAT", Color.WHITE, REFINED_NAVY);
        btnReturnSeatMap.setBounds(630, 400, 90, 40);
        btnReturnSeatMap.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        btnReturnSeatMap.setVisible(false);
        pnlForm.add(btnReturnSeatMap);

        lblReturnSeatNo = new JLabel("R: None");
        lblReturnSeatNo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblReturnSeatNo.setForeground(DataManager.SUNSET_ORANGE);
        lblReturnSeatNo.setBounds(725, 400, 85, 40);
        lblReturnSeatNo.setVisible(false);
        pnlForm.add(lblReturnSeatNo);

        // --- ROW 6: TRAVEL INSURANCE LAYER (445) ---
        chkInsurance = new JCheckBox("<html>Include Full Travel Insurance <font color='#FF7800'>(+P350)</font></html>");
        chkInsurance.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        chkInsurance.setForeground(TEXT_WHITE);
        chkInsurance.setOpaque(false);
        chkInsurance.setFocusPainted(false);
        chkInsurance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkInsurance.setIconTextGap(10); 
        chkInsurance.setBounds(445, 445, 365, 35);

        Icon unselectedIcon = new Icon() {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); 
                g2.fillRoundRect(x, y, 20, 20, 4, 4);
                g2.setColor(new Color(140, 155, 175)); 
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, 20, 20, 4, 4);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 20; }
            @Override public int getIconHeight() { return 20; }
        };

        Icon selectedIcon = new Icon() {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); 
                g2.fillRoundRect(x, y, 20, 20, 4, 4);
                g2.setColor(DataManager.SUNSET_ORANGE); 
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(x, y, 20, 20, 4, 4);
                g2.setColor(DataManager.SUNSET_ORANGE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 5, y + 10, x + 9, y + 14);  
                g2.drawLine(x + 9, y + 14, x + 16, y + 6); 
                g2.dispose();
            }
            @Override public int getIconWidth() { return 20; }
            @Override public int getIconHeight() { return 20; }
        };

        chkInsurance.setIcon(unselectedIcon);
        chkInsurance.setSelectedIcon(selectedIcon);
        pnlForm.add(chkInsurance);

        // --- ROW 7: ACTION BUTTONS (490) ---
        btnCancel = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnCancel.setBounds(40, 520, 365, 45);
        btnCancel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        pnlForm.add(btnCancel);

        btnConfirm = new ActionButton("CONFIRM & PROCEED", DataManager.SUNSET_ORANGE, Color.WHITE);
        btnConfirm.setBounds(445, 520, 365, 45);
        btnConfirm.setEnabled(false);
        pnlForm.add(btnConfirm);

        // --- CORE RUNTIME ACTION CONTROLLERS ---

        btnChangeFlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtFlightSearch.setEnabled(true);
                txtPassengerCountInput.setEnabled(true);
                btnVerify.setEnabled(true);
                btnChangeFlight.setEnabled(false);
                btnConfirm.setEnabled(false);
                isFlightVerified = false;
                targetFlightData = null;
                lblFlightStatusCard.setBackground(SOFT_CARD_NAVY);
                lblFlightStatusCard.setForeground(SUBTLE_GRAY);
                lblFlightStatusCard.setText("Awaiting localized validation input segment...");
                lblSummary.setText("Flight: None Selected | Base Price: Awaiting Verification");
            }
        });

        btnSeatMap.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { handleSeatSelection(); }
        });

        btnReturnSeatMap.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { handleReturnSeatSelection(); }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(PassengerDetailsFrame.this, "Discard progress and return to main interface?", "Cancel Operation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) { MainDashboardFrame.showCard("DASHBOARD"); }
            }
        });

        btnConfirm.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { handlePassengerValidationLoop(); }
        });

        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleVerifyAction();
            }
        });

        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override public void ancestorAdded(javax.swing.event.AncestorEvent event) { 
                startNewBookingSession(); 
            }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    // ── Exit confirmation dialog ──────────────────────────────────────────────
    private void showExitDialogPassenger() {
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

        dialog.getContentPane().add(pnlDialog);
        dialog.setVisible(true);
    }

    private void startNewBookingSession() {
        currentPassenger = 1;
        totalPassengers = 1;
        selectedSeat = "None";
        selectedReturnSeat = "None";
        lblSeatNo.setText("Seat: None");
        if (lblReturnSeatNo != null) lblReturnSeatNo.setText("R: None");
        clearFields();
        billingRecords.clear();
        btnConfirm.setText("CONFIRM & PROCEED");
        
        txtPassengerCountInput.setText("1");
        txtPassengerCountInput.setEnabled(true);
        txtFlightSearch.setText("");
        
        lblTitle.setText("PASSENGER DETAILS (Express Mode Entrance)");
        lblFlightStatusCard.setBackground(SOFT_CARD_NAVY);
        lblFlightStatusCard.setForeground(SUBTLE_GRAY);
        lblFlightStatusCard.setText("Awaiting validation sequence input...");
        isFlightVerified = false;
        targetFlightData = null;

        if (DataManager.selectedFlightData != null) {
            targetFlightData = DataManager.selectedFlightData;
            txtFlightSearch.setText(targetFlightData[0].toString());
            isFlightVerified = true;
            txtFlightSearch.setEnabled(false);
            txtPassengerCountInput.setEnabled(false);
            if (this.btnVerify != null) this.btnVerify.setEnabled(false);
            if (this.btnChangeFlight != null) this.btnChangeFlight.setEnabled(true);
            if (this.btnConfirm != null) this.btnConfirm.setEnabled(true);
            
            lblFlightStatusCard.setBackground(new Color(34, 163, 102)); 
            lblFlightStatusCard.setForeground(Color.WHITE);
            lblFlightStatusCard.setText(targetFlightData[0] + " | " + targetFlightData[2] + " \u2192 " + targetFlightData[3] + " | " + targetFlightData[5] + " | " + targetFlightData[6] + " base");

            lblTitle.setText("Passenger Registration & Booking (Passenger 1 of 1)");
            lblSummary.setText("Flight ID: " + targetFlightData[0] + " | Destination: " + targetFlightData[1] + " | Base Fare: " + targetFlightData[2]);

            // Trigger passport field update based on flight type
            if (targetFlightData.length > 7) {
                if (targetFlightData[7].toString().equalsIgnoreCase("international")) {
                    cbFlightType.setSelectedItem("International");
                } else {
                    cbFlightType.setSelectedItem("Domestic");
                }
            }
        } else {
            lblSummary.setText("Flight: None Selected | Base Price: Awaiting Verification");
            if (this.btnConfirm != null) this.btnConfirm.setEnabled(false);
            if (this.btnChangeFlight != null) this.btnChangeFlight.setEnabled(false);
        }
        revalidate();
        repaint();
    }

    // --- SEAT SELECTION: delegates to standalone dialog classes ---
    private void handleSeatSelection() {
        boolean isInternational = targetFlightData != null
            && targetFlightData[0].toString().contains("INT");
        boolean isBusinessMode  = cbClassType.getSelectedIndex() == 1;
        Frame   owner           = (Frame) SwingUtilities.getWindowAncestor(this);

        if (isInternational) {
            InternationalSeatMapDialog dlg =
                new InternationalSeatMapDialog(owner, isBusinessMode, selectedSeat);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            String result = dlg.getSelectedSeat();
            if (!result.equals("None")) {
                selectedSeat = result;
                if (cbTripType.getSelectedItem().toString().equals("Round Trip")) {
                    lblSeatNo.setText("D: " + selectedSeat);
                } else {
                    lblSeatNo.setText("Seat: " + selectedSeat);
                }
            }
        } else {
            DomesticSeatMapDialog dlg =
                new DomesticSeatMapDialog(owner, isBusinessMode, selectedSeat);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            String result = dlg.getSelectedSeat();
            if (!result.equals("None")) {
                selectedSeat = result;
                if (cbTripType.getSelectedItem().toString().equals("Round Trip")) {
                    lblSeatNo.setText("D: " + selectedSeat);
                } else {
                    lblSeatNo.setText("Seat: " + selectedSeat);
                }
            }
        }
    }

    private void handleReturnSeatSelection() {
        boolean isInternational = targetFlightData != null
            && targetFlightData[0].toString().contains("INT");
        boolean isBusinessMode  = cbClassType.getSelectedIndex() == 1;
        Frame   owner           = (Frame) SwingUtilities.getWindowAncestor(this);

        if (isInternational) {
            InternationalSeatMapDialog dlg =
                new InternationalSeatMapDialog(owner, isBusinessMode, selectedReturnSeat);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            String result = dlg.getSelectedSeat();
            if (!result.equals("None")) {
                selectedReturnSeat = result;
                lblReturnSeatNo.setText("R: " + selectedReturnSeat);
            }
        } else {
            DomesticSeatMapDialog dlg =
                new DomesticSeatMapDialog(owner, isBusinessMode, selectedReturnSeat);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            String result = dlg.getSelectedSeat();
            if (!result.equals("None")) {
                selectedReturnSeat = result;
                lblReturnSeatNo.setText("R: " + selectedReturnSeat);
            }
        }
    }



    private void handlePassengerValidationLoop() {
        if (!isFlightVerified || targetFlightData == null) {
            JOptionPane.showMessageDialog(this, "Operational Blockage! Please verify a valid flight segment key target code first.", "Verification Void", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtName.getText().trim().isEmpty() || txtAge.getText().trim().isEmpty() || txtContact.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fulfill all required fields (Name, Age, Contact)!", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbFlightType.getSelectedItem().toString().equals("International") && txtPassport.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Passport Number is required for International flights!", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cbDiscount.getSelectedItem().toString().equals("NONE") && txtDiscountID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Discount ID Number is required for the selected discount!", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbTripType.getSelectedItem().toString().equals("Round Trip")) {
            if (dateDeparture.getDate() == null || dateReturn.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select both Departure and Return dates for a Round Trip!", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dateDeparture.getDate().after(dateReturn.getDate())) {
                JOptionPane.showMessageDialog(this, "Return date cannot be earlier than the Departure date!", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (selectedSeat.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please allocate an structural aircraft seat number assignment!", "Operational Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbTripType.getSelectedItem().toString().equals("Round Trip") && selectedReturnSeat.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please allocate a return seat number assignment!", "Operational Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(txtAge.getText().trim());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please execute a valid numerical indicator for passenger age!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double baseFare = 2000; 
        if (targetFlightData != null) {
            try {
                String rawPrice = targetFlightData[6].toString().replaceAll("[^0-9]", "");
                baseFare = Double.parseDouble(rawPrice);
            } catch (Exception e) {}
        }

        double classFee = cbClassType.getSelectedIndex() == 1 ? 4500 : 0;
        double insuranceFee = chkInsurance.isSelected() ? 350 : 0;
        
        double baggageFee = 0;
        int bagIndex = cbBaggage.getSelectedIndex();
        if (bagIndex == 1) baggageFee = 550;
        else if (bagIndex == 2) baggageFee = 950;
        else if (bagIndex == 3) baggageFee = 1400;

        double cargoFee = 0;
        int cargoIndex = cbSpecialCargo.getSelectedIndex();
        if (cargoIndex == 1) cargoFee = 15000;
        else if (cargoIndex == 2) cargoFee = 3500;
        else if (cargoIndex == 3) cargoFee = 1200;
        else if (cargoIndex == 4) cargoFee = 2500;

        double travelTax = 1620; 

        double subtotalSubjectToDiscount = baseFare + classFee;
        double discountAmount = 0;
        String discType = cbDiscount.getSelectedItem().toString();
        if (discType.startsWith("SENIOR") || discType.startsWith("PWD") || discType.startsWith("STUDENT")) {
            discountAmount = subtotalSubjectToDiscount * 0.20;
        }

        double individualTotal = (subtotalSubjectToDiscount - discountAmount) + baggageFee + cargoFee + insuranceFee + travelTax;

        String passengerSummary = "Passenger #" + currentPassenger + " [Seat " + selectedSeat + "]: " + txtName.getText().trim() + "\n" +
                                  "  - Base Fare & Cabin [" + cbClassType.getSelectedItem().toString().split("\\[|\\+")[0].trim() + "]: P" + subtotalSubjectToDiscount + "\n" +
                                  "  - Discount Applied [" + discType + "]: -P" + discountAmount + "\n" +
                                  "  - Add-on Baggage: P" + baggageFee + "\n" +
                                  "  - Special Cargo Fees: P" + cargoFee + "\n" +
                                  "  - Emergency Travel Insurance: P" + insuranceFee + "\n" +
                                  "  - Mandatory Required Travel Tax: P" + travelTax + "\n" +
                                  "  * Total Balance due for Passenger: P" + individualTotal + "\n\n";
        billingRecords.add(passengerSummary);

        com.system.models.Passenger newPassenger = new com.system.models.Passenger(
            txtName.getText().trim(), txtContact.getText().trim(), txtPassport.getText().trim(), age
        );
        DataManager.bookedPassengers.add(newPassenger);

        String newTxnId = "TXN-" + (10042 + DataManager.bookingRecords.size());
        String routeStr = targetFlightData[2] + " -> " + targetFlightData[3];
        DataManager.bookingRecords.add(new Object[]{
            newTxnId, 
            newPassenger.getName(), 
            targetFlightData[0].toString(), 
            routeStr, 
            selectedSeat, 
            String.valueOf((int)individualTotal), 
            "CONFIRMED"
        });

        JOptionPane.showMessageDialog(this, "Operational details verified and logged for Passenger " + currentPassenger + "!");

        if (currentPassenger < totalPassengers) {
            currentPassenger++;
            lblTitle.setText("Passenger Registration & Booking (Passenger " + currentPassenger + " of " + totalPassengers + ")");
            clearFields();
            selectedSeat = "None";
            lblSeatNo.setText("Seat: None");
            cbClassType.setSelectedIndex(0);
            cbBaggage.setSelectedIndex(0);
            cbSpecialCargo.setSelectedIndex(0);
            cbDiscount.setSelectedIndex(0);
            chkInsurance.setSelected(false);

            if (currentPassenger == totalPassengers) {
                btnConfirm.setText("PROCEED TO BILLING");
            }
        } else {
            double grandTotal = 0;
            for (String record : billingRecords) {
                String[] lines = record.split("\n");
                for (String line : lines) {
                    if (line.contains("Total Balance due for Passenger")) {
                        try {
                            grandTotal += Double.parseDouble(line.replaceAll("[^0-9.]", ""));
                        } catch (Exception e) {}
                        break;
                    }
                }
            }
            
            String[] paymentOptions = {"Cash", "Credit Card", "GCash"};
            int paymentChoice = JOptionPane.showOptionDialog(this, 
                "Please select your payment method to settle the total amount of P" + String.format("%,.2f", grandTotal), 
                "Payment Processing", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                null, paymentOptions, paymentOptions[0]);
                
            String selectedPayment = paymentChoice == -1 ? "Cash (Defaulted)" : paymentOptions[paymentChoice];
            
            displayModernInvoice(grandTotal, selectedPayment);
        }
    }

    private void displayModernInvoice(double grandTotal, String selectedPayment) {
        JDialog invoiceDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Digital Invoice", true);
        invoiceDialog.setSize(550, 650);
        invoiceDialog.setLocationRelativeTo(this);
        invoiceDialog.setUndecorated(true);
        invoiceDialog.setBackground(new Color(0, 0, 0, 0));

        RoundedPanel pnlMain = new RoundedPanel();
        pnlMain.setCornerRadius(30);
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // --- Header Section ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JLabel lblLogo = new JLabel("✈ BIYAHENG LANGIT", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(DataManager.SUNSET_ORANGE);
        pnlHeader.add(lblLogo, BorderLayout.NORTH);
        
        JLabel lblSub = new JLabel("Official E-Ticket & Billing Statement", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(120, 130, 140));
        pnlHeader.add(lblSub, BorderLayout.CENTER);
        
        pnlMain.add(pnlHeader, BorderLayout.NORTH);

        // --- Receipt Body (HTML JEditorPane) ---
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:\"Segoe UI\", sans-serif; margin: 15px; color: #2C3E50;'>");
        
        html.append("<div style='border-bottom: 2px solid #FF7800; margin-bottom: 15px; padding-bottom: 5px;'>");
        html.append("<h2 style='color: #1A2A40; margin: 0;'>Flight Route: ").append(targetFlightData[2]).append(" &rarr; ").append(targetFlightData[3]).append("</h2>");
        html.append("<p style='margin: 3px 0; color: #555;'><strong>Flight ID:</strong> ").append(targetFlightData[0]).append(" | <strong>Date:</strong> ").append(targetFlightData[4]).append("</p>");
        html.append("</div>");
        
        for (String record : billingRecords) {
            String[] lines = record.split("\n");
            html.append("<div style='background-color: #F8F9FA; padding: 12px; border-radius: 8px; margin-bottom: 12px; border: 1px solid #E9ECEF;'>");
            
            // Passenger Name and Seat Header
            html.append("<h3 style='color:#FF7800; margin-top:0; margin-bottom: 8px;'>").append(lines[0]).append("</h3>"); 
            
            // Line items
            html.append("<table width='100%' style='font-size: 11px; color: #555;'>");
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty() || line.startsWith("*")) continue;
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    html.append("<tr>");
                    html.append("<td style='padding: 2px 0;'>").append(parts[0].replace("-", "").trim()).append("</td>");
                    html.append("<td align='right' style='font-weight: bold;'>").append(parts[1].trim()).append("</td>");
                    html.append("</tr>");
                }
            }
            html.append("</table>");
            
            // Subtotal Line
            for (String line : lines) {
                if (line.contains("Total Balance")) {
                    html.append("<div style='margin-top: 10px; border-top: 1px solid #DEE2E6; padding-top: 8px; text-align: right;'>");
                    html.append("<strong style='color:#1A2A40; font-size: 14px;'>Total: ").append(line.split(":")[1].trim()).append("</strong>");
                    html.append("</div>");
                    break;
                }
            }
            html.append("</div>");
        }
        
        // --- Grand Total Footer inside HTML ---
        html.append("<div style='margin-top: 25px; border-top: 3px dashed #BDC3C7; padding-top: 20px;'>");
        html.append("<table width='100%'><tr>");
        html.append("<td><span style='font-size: 14px; color:#7F8C8D;'>PAYMENT METHOD</span><br><strong style='color:#1A2A40; font-size: 18px;'>").append(selectedPayment).append("</strong></td>");
        html.append("<td align='right'><span style='font-size: 14px; color:#7F8C8D;'>GRAND TOTAL DUE</span><br><h1 style='color:#FF7800; margin: 0; font-size: 26px;'>P").append(String.format("%,.2f", grandTotal)).append("</h1></td>");
        html.append("</tr></table></div>");
        
        html.append("</body></html>");

        JEditorPane editorPane = new JEditorPane("text/html", html.toString());
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        pnlMain.add(scrollPane, BorderLayout.CENTER);

        // --- Action Footer ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlFooter.setBackground(Color.WHITE);
        
        ActionButton btnPrint = new ActionButton("FINISH & PRINT E-TICKET", DataManager.SUNSET_ORANGE, Color.WHITE);
        btnPrint.setPreferredSize(new Dimension(300, 50));
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPrint.addActionListener(e -> {
            invoiceDialog.dispose();
            JOptionPane.showMessageDialog(this, "Booking Successful! Flight Tickets Generated and Printed.");
            MainDashboardFrame.showCard("DASHBOARD");
        });
        
        pnlFooter.add(btnPrint);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        invoiceDialog.setContentPane(pnlMain);
        invoiceDialog.setVisible(true);
    }

    private void styleFormLabel(JLabel label, int x, int y) {
        label.setBounds(x, y, 250, 20);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(180, 195, 220));
    }

    private void clearFields() {
        txtName.setText("");
        txtAge.setText("");
        txtContact.setText("");
        txtPassport.setText("");
        if (cbNationality != null) cbNationality.setSelectedIndex(0);
        if (cbTripType != null) cbTripType.setSelectedIndex(0);
        if (cbSpecialCargo != null) cbSpecialCargo.setSelectedIndex(0);
        if (txtDiscountID != null) txtDiscountID.setText("");
        if (dateDeparture != null) dateDeparture.setDate(null);
        if (dateReturn != null) dateReturn.setDate(null);
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items, int x, int y, int w, int h) {
        JComboBox<T> cb = new JComboBox<>(items) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cb.setBounds(x, y, w, h);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setOpaque(false);
        cb.setBackground(Color.WHITE);
        cb.setFocusable(false); 

        cb.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrowBtn = new JButton() {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.WHITE); 
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        
                        g2.setColor(new Color(80, 95, 110));
                        int[] xPoints = {getWidth() / 2 - 5, getWidth() / 2 + 5, getWidth() / 2};
                        int[] yPoints = {getHeight() / 2 - 2, getHeight() / 2 - 2, getHeight() / 2 + 4};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                arrowBtn.setBorderPainted(false);
                arrowBtn.setContentAreaFilled(false);
                arrowBtn.setFocusPainted(false);
                return arrowBtn;
            }
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(Color.WHITE);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                if (isSelected) {
                    l.setBackground(new Color(240, 245, 255));
                    l.setForeground(REFINED_NAVY);
                } else {
                    l.setBackground(Color.WHITE);
                    l.setForeground(new Color(50, 50, 50));
                }
                return l;
            }
        });
        
        cb.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        return cb;
    }

    private void handleVerifyAction() {
        String inputID = txtFlightSearch.getText().trim().toUpperCase();
        String rawCount = txtPassengerCountInput.getText().trim();

        if (inputID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please execute a valid Flight ID string key first!", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            totalPassengers = Integer.parseInt(rawCount);
            if (totalPassengers < 1) {
                JOptionPane.showMessageDialog(this, "Passenger count must register at least 1 traveler.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please provide a valid number for passenger count!", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        for (Object[] flight : DataManager.getMockFlightsDB()) {
            if (flight[0].toString().equalsIgnoreCase(inputID)) {
                found = true;
                String status = flight[5].toString();
                if (status.equals("FULL")) {
                    lblFlightStatusCard.setBackground(new Color(220, 40, 40));
                    lblFlightStatusCard.setForeground(Color.WHITE);
                    lblFlightStatusCard.setText("REJECTED: Flight is completely FULL!");
                    isFlightVerified = false;
                    return;
                }
                if (status.equals("BOARDING")) {
                    lblFlightStatusCard.setBackground(DataManager.SUNSET_ORANGE);
                    lblFlightStatusCard.setForeground(Color.WHITE);
                    lblFlightStatusCard.setText("LOCKED: Aircraft is actively BOARDING!");
                    isFlightVerified = false;
                    return;
                }
                try {
                    int seatsAvail = Integer.parseInt(flight[10].toString());
                    if (seatsAvail < totalPassengers) {
                        lblFlightStatusCard.setBackground(new Color(220, 40, 40));
                        lblFlightStatusCard.setForeground(Color.WHITE);
                        lblFlightStatusCard.setText("REJECTED: Not enough seats (" + seatsAvail + " left)");
                        isFlightVerified = false;
                        return;
                    }
                } catch (Exception ex) {}

                targetFlightData = flight;
                isFlightVerified = true;
                txtFlightSearch.setEnabled(false);
                txtPassengerCountInput.setEnabled(false);
                btnVerify.setEnabled(false);
                btnChangeFlight.setEnabled(true);
                btnConfirm.setEnabled(true);

                lblFlightStatusCard.setBackground(new Color(34, 163, 102));
                lblFlightStatusCard.setForeground(Color.WHITE);
                lblFlightStatusCard.setText(flight[0] + " | " + flight[2] + " \u2192 " + flight[3] + " | " + flight[5] + " | " + flight[6] + " base");
                lblTitle.setText("Passenger Registration & Booking (" + currentPassenger + " of " + totalPassengers + ")");
                lblSummary.setText("Flight ID: " + flight[0] + " | Destination: " + flight[3] + " | Base Fare: " + flight[6]);

                if (flight.length > 7) {
                    if (flight[7].toString().equalsIgnoreCase("international")) {
                        cbFlightType.setSelectedItem("International");
                    } else {
                        cbFlightType.setSelectedItem("Domestic");
                    }
                }
                break;
            }
        }
        if (!found) {
            lblFlightStatusCard.setBackground(new Color(220, 40, 40));
            lblFlightStatusCard.setForeground(Color.WHITE);
            lblFlightStatusCard.setText("DENIED: Flight ID not found!");
            isFlightVerified = false;
            targetFlightData = null;
        }
    }
}
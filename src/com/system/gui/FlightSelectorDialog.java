package com.system.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightSelectorDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Color APP_DARK_NAVY = new Color(20, 30, 55);
    private final Color ACCENT_ORANGE  = DataManager.SUNSET_ORANGE;
    private final Color SUBTLE_GRAY    = new Color(160, 175, 200);

    private JComboBox<String> cbFlightStatus;
    private JComboBox<String> cbFlightCategory;
    private JTextField        txtSearch;
    private com.toedter.calendar.JDateChooser dcDateFilter;

    private JTable            tblFlights;
    private DefaultTableModel tableModel;
    private JScrollPane       scrollPane;

    private JLabel  lblSelectedFlight;
    private Object[] selectedFlightData = null;
    private boolean isConfirmed = false;

    public FlightSelectorDialog(Frame owner, String initialCategory, Date initialDate) {
        super(owner, "Select Departure Flight", true);
        setSize(920, 580);
        setLocationRelativeTo(owner);
        initComponents(initialCategory, initialDate);
        loadFlightRecordsData();
    }

    private void initComponents(String initialCategory, Date initialDate) {
        // Root Panel
        JPanel pnlRoot = new JPanel(new BorderLayout());
        pnlRoot.setBackground(APP_DARK_NAVY);
        pnlRoot.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(pnlRoot);

        // Header Panel
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("━ SELECT DEPARTURE FLIGHT ━", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblSub = new JLabel("Search, filter, and double-click the target flight to confirm selection.", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSub.setForeground(SUBTLE_GRAY);
        pnlHeader.add(lblSub, BorderLayout.SOUTH);
        pnlRoot.add(pnlHeader, BorderLayout.NORTH);

        // Center Panel (containing Filters & Table)
        JPanel pnlCenter = new JPanel(null);
        pnlCenter.setOpaque(false);
        pnlRoot.add(pnlCenter, BorderLayout.CENTER);

        // --- Filters Line ---
        cbFlightStatus = new JComboBox<>(new String[]{"All Statuses", "Available", "Delayed", "Cancelled", "Fully Booked"});
        cbFlightStatus.setBounds(10, 10, 140, 36);
        cbFlightStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFlightStatus.setBackground(Color.WHITE);
        cbFlightStatus.setFocusable(false);
        pnlCenter.add(cbFlightStatus);

        cbFlightCategory = new JComboBox<>(new String[]{"All Flights", "Domestic", "International"});
        cbFlightCategory.setBounds(160, 10, 130, 36);
        cbFlightCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFlightCategory.setBackground(Color.WHITE);
        cbFlightCategory.setFocusable(false);
        if (initialCategory != null) {
            cbFlightCategory.setSelectedItem(initialCategory);
        }
        pnlCenter.add(cbFlightCategory);

        JLabel lblDate = new JLabel("Date:", SwingConstants.RIGHT);
        lblDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblDate.setForeground(SUBTLE_GRAY);
        lblDate.setBounds(300, 10, 40, 36);
        pnlCenter.add(lblDate);

        dcDateFilter = new com.toedter.calendar.JDateChooser();
        dcDateFilter.setBounds(350, 10, 140, 36);
        dcDateFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dcDateFilter.setBackground(Color.WHITE);
        if (initialDate != null) {
            dcDateFilter.setDate(initialDate);
        }
        pnlCenter.add(dcDateFilter);

        JLabel lblSearch = new JLabel("Search:", SwingConstants.RIGHT);
        lblSearch.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSearch.setForeground(SUBTLE_GRAY);
        lblSearch.setBounds(500, 10, 50, 36);
        pnlCenter.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(560, 10, 310, 36);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        pnlCenter.add(txtSearch);

        // --- Table ---
        String[] columns = {"Flight ID", "Date", "Origin", "Destination", "Time", "Status", "Base Price", "Airline", "Gate", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblFlights = new JTable(tableModel);
        tblFlights.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        tblFlights.setRowHeight(34);
        tblFlights.setGridColor(new Color(230, 235, 240));
        tblFlights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFlights.setRowSelectionAllowed(true);
        tblFlights.setShowGrid(true);

        JTableHeader tblHeader = tblFlights.getTableHeader();
        tblHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblHeader.setBackground(Color.WHITE);
        tblHeader.setForeground(new Color(60, 75, 100));
        tblHeader.setPreferredSize(new Dimension(tblHeader.getWidth(), 35));

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                if (c instanceof JLabel) {
                    JLabel lbl = (JLabel) c;
                    lbl.setOpaque(true);
                    lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    lbl.setBackground(isSelected ? new Color(210, 225, 245) : Color.WHITE);
                    if (column == 5 && value != null) {
                        String s = value.toString().toUpperCase();
                        if      (s.contains("AVAIL"))   lbl.setForeground(new Color(20, 140, 60));
                        else if (s.contains("FULL") || s.contains("CANCEL")) lbl.setForeground(new Color(220, 40, 40));
                        else if (s.contains("DELAY"))   lbl.setForeground(new Color(235, 110, 0));
                        else lbl.setForeground(isSelected ? APP_DARK_NAVY : new Color(50, 60, 75));
                    } else {
                        lbl.setForeground(isSelected ? APP_DARK_NAVY : new Color(50, 60, 75));
                    }
                }
                return c;
            }
        };
        for (int i = 0; i < tblFlights.getColumnCount(); i++) {
            tblFlights.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
        }

        scrollPane = new JScrollPane(tblFlights);
        scrollPane.setBounds(10, 60, 860, 280);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(215, 220, 225), 1));
        pnlCenter.add(scrollPane);

        // --- Selection Strip ---
        JPanel pnlSelectedStrip = new JPanel(new BorderLayout());
        pnlSelectedStrip.setBackground(Color.WHITE);
        pnlSelectedStrip.setBounds(10, 355, 860, 36);
        pnlSelectedStrip.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        
        lblSelectedFlight = new JLabel("No flight selected. Double-click a row to confirm instantly.");
        lblSelectedFlight.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSelectedFlight.setForeground(new Color(50, 65, 90));
        pnlSelectedStrip.add(lblSelectedFlight, BorderLayout.CENTER);
        pnlCenter.add(pnlSelectedStrip);

        // --- Bottom Actions Panel ---
        JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(10, 0, 0, 0));

        ActionButton btnCancel = new ActionButton("CANCEL", Color.WHITE, Color.RED);
        btnCancel.setPreferredSize(new Dimension(0, 45));
        btnCancel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btnCancel.addActionListener(e -> dispose());
        pnlFooter.add(btnCancel);

        ActionButton btnSelect = new ActionButton("CONFIRM FLIGHT SELECTION", ACCENT_ORANGE, Color.WHITE);
        btnSelect.addActionListener(e -> confirmAndClose());
        pnlFooter.add(btnSelect);

        pnlRoot.add(pnlFooter, BorderLayout.SOUTH);

        // Component resizing to dynamically fix absolute bounds at layout time
        pnlCenter.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = pnlCenter.getWidth();
                int h = pnlCenter.getHeight();
                
                txtSearch.setBounds(w - 320, 10, 310, 36);
                lblSearch.setBounds(w - 380, 10, 50, 36);
                
                dcDateFilter.setBounds(w - 530, 10, 140, 36);
                lblDate.setBounds(w - 580, 10, 40, 36);
                
                scrollPane.setBounds(10, 60, w - 20, h - 110);
                pnlSelectedStrip.setBounds(10, h - 45, w - 20, 36);
            }
        });

        // Event listeners
        cbFlightStatus.addActionListener(e -> loadFlightRecordsData());
        cbFlightCategory.addActionListener(e -> loadFlightRecordsData());
        dcDateFilter.addPropertyChangeListener("date", evt -> loadFlightRecordsData());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { loadFlightRecordsData(); }
        });

        tblFlights.getSelectionModel().addListSelectionListener(e -> {
            int row = tblFlights.getSelectedRow();
            if (row != -1) {
                String id = tblFlights.getValueAt(row, 0).toString();
                String origin = tblFlights.getValueAt(row, 2).toString();
                String dest = tblFlights.getValueAt(row, 3).toString();
                String date = tblFlights.getValueAt(row, 1).toString();
                lblSelectedFlight.setText("Selected Flight: " + id + " | " + origin + " ➔ " + dest + " | Date: " + date);
            }
        });

        tblFlights.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    confirmAndClose();
                }
            }
        });
    }

    private void loadFlightRecordsData() {
        tableModel.setRowCount(0);
        String statusFilter = cbFlightStatus.getSelectedItem().toString().toUpperCase();
        String catFilter = cbFlightCategory.getSelectedItem().toString().toUpperCase();
        Date dateVal = dcDateFilter.getDate();
        String selectedDateStr = dateVal != null ? new SimpleDateFormat("dd-MM-yy").format(dateVal) : null;
        String query = txtSearch.getText().trim().toLowerCase();

        for (Object[] row : DataManager.getFlightsDB()) {
            String id       = row[0].toString();
            String date     = row[1].toString();
            String origin   = row[2].toString();
            String dest     = row[3].toString();
            String time     = row[4].toString();
            String status   = row[5].toString();
            String price    = row[6].toString();
            String airline  = row.length > 8 ? row[8].toString() : "PAL";
            String gate     = row.length > 9 ? row[9].toString() : "T3";
            String capacity = row.length > 10 ? row[10].toString() : "48";
            String flightType = row.length > 7 ? row[7].toString() : "domestic";

            // Category Filter
            if (catFilter.equals("DOMESTIC") && !flightType.equalsIgnoreCase("domestic")) continue;
            if (catFilter.equals("INTERNATIONAL") && !flightType.equalsIgnoreCase("international")) continue;

            // Status Filter
            if (!statusFilter.equals("ALL STATUSES")) {
                if (statusFilter.equals("AVAILABLE") && !status.toUpperCase().contains("AVAILABLE")) continue;
                if (statusFilter.equals("DELAYED") && !status.toUpperCase().contains("DELAY")) continue;
                if (statusFilter.equals("CANCELLED") && !status.toUpperCase().contains("CANCEL")) continue;
                if (statusFilter.equals("FULLY BOOKED") && !status.toUpperCase().contains("FULL")) continue;
            }

            // Date Filter
            if (selectedDateStr != null && !date.equalsIgnoreCase(selectedDateStr)) continue;

            // Text search
            String searchCorpus = (id + " " + date + " " + origin + " " + dest + " " + airline).toLowerCase();
            if (!query.isEmpty() && !searchCorpus.contains(query)) continue;

            String fullOrigin = DataManager.getAirportFullName(origin);
            String fullDest = DataManager.getAirportFullName(dest);

            tableModel.addRow(new Object[]{
                id, date, fullOrigin, fullDest, time, status, price, airline, gate, capacity
            });
        }
    }

    private void confirmAndClose() {
        int selectedRow = tblFlights.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight row first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String flightId = tblFlights.getValueAt(selectedRow, 0).toString();
        Object[] rawFlight = null;
        for (Object[] f : DataManager.getFlightsDB()) {
            if (f[0].toString().equals(flightId)) {
                rawFlight = f;
                break;
            }
        }

        if (rawFlight != null) {
            String status = rawFlight[5].toString().toUpperCase();
            if (status.contains("FULL")) {
                JOptionPane.showMessageDialog(this,
                    "<html><font color='red'><b>Flight Full:</b></font><br>This flight is fully booked.</html>",
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (status.contains("CANCEL")) {
                JOptionPane.showMessageDialog(this,
                    "<html><font color='red'><b>Flight Cancelled:</b></font><br>This flight has been cancelled.</html>",
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedFlightData = rawFlight;
            isConfirmed = true;
            dispose();
        }
    }

    public boolean isConfirmed() { return isConfirmed; }
    public Object[] getSelectedFlightData() { return selectedFlightData; }
}

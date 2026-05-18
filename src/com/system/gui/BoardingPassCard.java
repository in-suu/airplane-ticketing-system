package com.system.gui;

import javax.swing.*;
import com.system.models.DataManager;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardingPassCard extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final String flightId;
    private final String origin;
    private final String dest;
    private final String cabinClass;
    private final String seat;
    private final String ticketLabel;
    private final String formattedDate;
    private final Date dateObj;

    private final Color TICKET_BG      = new Color(250, 252, 255);
    private final Color ACCENT_ORANGE  = DataManager.SUNSET_ORANGE;
    private final Color ACCENT_GOLD    = new Color(212, 175, 55); // First Class Gold
    private final Color TEXT_DARK      = new Color(40, 50, 70);
    private final Color TEXT_MUTED     = new Color(130, 140, 160);
    private final Color APP_DARK_NAVY  = new Color(15, 23, 42);

    public BoardingPassCard(String name, String flightId, String origin, String dest, 
                            String cabinClass, Date date, String seat, String ticketLabel) {
        this.name = name.toUpperCase();
        this.flightId = flightId.toUpperCase();
        this.origin = origin.toUpperCase();
        this.dest = dest.toUpperCase();
        this.cabinClass = cabinClass.toUpperCase();
        this.seat = seat.toUpperCase();
        this.ticketLabel = ticketLabel.toUpperCase();
        this.formattedDate = new SimpleDateFormat("EEE, dd MMM yyyy").format(date);
        this.dateObj = date;
        
        setPreferredSize(new Dimension(760, 250));
        setMinimumSize(new Dimension(760, 250));
        setMaximumSize(new Dimension(760, 250));
        setOpaque(false);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        
        // Draw ticket background
        g2.setColor(TICKET_BG);
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 20, 20));
        
        // Draw Ticket Left Margin Accent Line
        boolean isFirstClass = cabinClass.contains("FIRST");
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        g2.fillRect(0, 0, 8, h);

        // Tear boundary (Stub at x = 530)
        int tearX = 530;
        
        // Draw Dashed Tear Line
        g2.setColor(new Color(200, 210, 225));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 6f}, 0f));
        g2.drawLine(tearX, 0, tearX, h);
        
        // Draw Semi-Circular Punch Holes at the top and bottom of the tear line
        g2.setColor(APP_DARK_NAVY);
        g2.fillArc(tearX - 10, -10, 20, 20, 180, 180); // Top punch
        g2.fillArc(tearX - 10, h - 10, 20, 20, 0, 180);  // Bottom punch

        // Draw MAIN BOARDING PASS CONTENTS
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        g2.drawString(ticketLabel, 35, 25);
        
        // Labels header row
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("FLIGHT NO.", 35, 52);
        g2.setColor(TEXT_DARK);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        g2.drawString(flightId, 35, 70);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("CABIN CLASS", 240, 52);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        g2.setColor(isFirstClass ? ACCENT_GOLD : TEXT_DARK);
        g2.drawString(cabinClass, 240, 70);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("GATE / BOARDING TIME", 380, 52);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        g2.setColor(TEXT_DARK);
        g2.drawString("GATE 3A / 45m prior", 380, 70);

        // Route Large Codes with expanded horizontal gap to prevent collisions
        g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        g2.setColor(TEXT_DARK);
        g2.drawString(origin, 35, 120);
        g2.drawString(dest, 240, 120);
        
        // Draw modern vector flight path instead of Unicode character to avoid square-box font rendering issues
        g2.setColor(new Color(200, 210, 225));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{5f, 4f}, 0f));
        g2.drawLine(135, 107, 225, 107);
        
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        int[] arrowX = {176, 186, 176};
        int[] arrowY = {101, 107, 113};
        g2.fillPolygon(arrowX, arrowY, 3);
        
        // Route details below code
        g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        g2.setColor(TEXT_MUTED);
        g2.drawString(getAirportName(origin), 35, 142);
        g2.drawString(getAirportName(dest), 240, 142);

        // Details Box
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.drawString("PASSENGER NAME", 35, 180);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        g2.setColor(TEXT_DARK);
        g2.drawString(name, 35, 198);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("DEPARTURE DATE", 240, 180);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        g2.setColor(TEXT_DARK);
        g2.drawString(formattedDate, 240, 198);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("SEAT NO.", 380, 180);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        g2.drawString(seat, 380, 198);

        // STUB SECTION (Right of tearX)
        int stubStart = tearX + 25;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        g2.drawString("BOARDING STUB", stubStart, 25);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("PASSENGER", stubStart, 48);
        g2.setColor(TEXT_DARK);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        String truncName = name.length() > 15 ? name.substring(0, 13) + "..." : name;
        g2.drawString(truncName, stubStart, 63);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_MUTED);
        g2.drawString("ROUTE", stubStart, 83);
        g2.setColor(TEXT_DARK);
        g2.drawString(origin + " -> " + dest, stubStart, 98);

        g2.setColor(TEXT_MUTED);
        g2.drawString("FLIGHT", stubStart, 118);
        g2.setColor(TEXT_DARK);
        g2.drawString(flightId, stubStart, 133);

        g2.setColor(TEXT_MUTED);
        g2.drawString("DATE", stubStart + 95, 118);
        g2.setColor(TEXT_DARK);
        String shortDate = new SimpleDateFormat("dd MMM yy").format(dateObj);
        g2.drawString(shortDate, stubStart + 95, 133);

        g2.setColor(TEXT_MUTED);
        g2.drawString("CLASS", stubStart, 153);
        g2.setColor(isFirstClass ? ACCENT_GOLD : TEXT_DARK);
        String truncClass = cabinClass.replace(" CLASS", "").replace(" [BASE]", "");
        g2.drawString(truncClass, stubStart, 168);

        g2.setColor(TEXT_MUTED);
        g2.drawString("SEAT", stubStart + 95, 153);
        g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        g2.setColor(isFirstClass ? ACCENT_GOLD : ACCENT_ORANGE);
        g2.drawString(seat, stubStart + 95, 168);

        // Barcode Drawing
        drawBarcode(g2, stubStart, 185, 175, 35);
        g2.dispose();
    }

    private String getAirportName(String code) {
        switch(code) {
            case "MNL": return "Byaheng Langit Intl (MNL)";
            case "CEB": return "Mactan-Cebu Intl (CEB)";
            case "DVO": return "Francisco Bangoy Intl (DVO)";
            case "PPS": return "Puerto Princesa Intl (PPS)";
            case "MPH": return "Caticlan Boracay (MPH)";
            case "ILO": return "Iloilo International (ILO)";
            case "NRT": return "Narita Intl Tokyo (NRT)";
            case "SIN": return "Changi Airport (SIN)";
            case "LAX": return "Los Angeles Intl (LAX)";
            case "LHR": return "London Heathrow (LHR)";
            default: return code;
        }
    }

    private void drawBarcode(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.BLACK);
        int currentX = x;
        java.util.Random rand = new java.util.Random(flightId.hashCode() + seat.hashCode());
        while (currentX < x + width) {
            int w = rand.nextInt(3) + 1; // 1-3 px
            int gap = rand.nextInt(3) + 1; // 1-3 px
            if (currentX + w > x + width) w = x + width - currentX;
            g2.fillRect(currentX, y, w, height);
            currentX += w + gap;
        }
    }
}

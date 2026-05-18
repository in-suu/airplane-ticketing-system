package com.system.models;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class DataManager {
    public static final Color SUNSET_ORANGE = new Color(255, 140, 0);

    public static Object[] selectedFlightData; 
    public static ArrayList<Passenger> bookedPassengers = new ArrayList<>();

    // Simulated fallback arrays have been completely removed. Database only!

    // Helper to translate airport acronyms to full location names
    public static String getAirportFullName(String code) {
        if (code == null) return "";
        switch (code.toUpperCase()) {
            case "MNL": return "Manila";
            case "CEB": return "Cebu";
            case "DVO": return "Davao";
            case "PPS": return "Puerto Princesa";
            case "MPH": return "Caticlan Boracay";
            case "ILO": return "Iloilo";
            case "BCD": return "Bacolod";
            case "KLO": return "Kalibo";
            case "TAC": return "Tacloban";
            case "CGY": return "Cagayan de Oro";
            case "TAG": return "Bohol-Panglao";
            case "GES": return "General Santos";
            case "NRT": return "Tokyo Narita";
            case "SIN": return "Singapore";
            case "LAX": return "Los Angeles";
            case "LHR": return "London Heathrow";
            case "HKG": return "Hong Kong";
            case "ICN": return "Seoul Incheon";
            case "KUL": return "Kuala Lumpur";
            case "BKK": return "Bangkok";
            case "SYD": return "Sydney";
            case "DXB": return "Dubai";
            default: return code;
        }
    }

    public static Object[][] getFlightsDB() {
        return queryFlightsFromMySQL();
    }

    public static Object[][] getBookingsDB() {
        return queryBookingsFromMySQL();
    }

    public static void bookPassenger(String txnId, String name, String flightId, String seat, double price, String status) {
        insertBookingToMySQL(txnId, name, flightId, seat, price, status);
    }

    public static void cancelBooking(String txnId) {
        updateBookingStatusInMySQL(txnId, "CANCELLED");
    }

    public static ArrayList<String> getOccupiedSeats(String flightId) {
        ArrayList<String> occupied = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT seat FROM bookings WHERE flight_id = ? AND status <> 'CANCELLED'")) {
            pstmt.setString(1, flightId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    occupied.add(rs.getString("seat").toUpperCase());
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error querying occupied seats: " + e.getMessage());
        }
        return occupied;
    }

    // --- PRIVATE MYSQL HELPERS ---

    private static Object[][] queryFlightsFromMySQL() {
        ArrayList<Object[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flights ORDER BY departure_date ASC, departure_time ASC")) {
            
            SimpleDateFormat uiDateFmt = new SimpleDateFormat("dd-MM-yy");
            NumberFormat priceFmt = NumberFormat.getIntegerInstance();

            while (rs.next()) {
                String id = rs.getString("flight_id");
                Date depDate = rs.getDate("departure_date");
                String dateStr = depDate != null ? uiDateFmt.format(depDate) : "";
                String origin = rs.getString("origin_code");
                String dest = rs.getString("dest_code");
                
                java.sql.Time sqlTime = rs.getTime("departure_time");
                String timeStr = "";
                if (sqlTime != null) {
                    timeStr = new SimpleDateFormat("h:mm a").format(sqlTime);
                }
                
                String status = rs.getString("status");
                double price = rs.getDouble("price");
                String priceStr = "P" + priceFmt.format(price);
                
                String category = rs.getString("category");
                String airline = rs.getString("airline");
                String gate = rs.getString("gate");
                String seats = String.valueOf(rs.getInt("seats_available"));

                // Deterministically inject simulated statuses based on flight ID hash
                Random rand = new Random(id.hashCode() + (dateStr != null ? dateStr.hashCode() : 0));
                String randomSimStatus = getRandomStatus(rand);
                if (randomSimStatus.equals("DELAYED")) {
                    String newTime = getDelayedTime(timeStr, rand);
                    timeStr = timeStr + "(" + newTime + ")";
                    status = "DELAYED";
                } else if (randomSimStatus.equals("CANCELLED")) {
                    status = "CANCELLED";
                } else if (randomSimStatus.equals("FULLY BOOKED")) {
                    status = "FULLY BOOKED";
                    seats = "0";
                }

                list.add(new Object[]{id, dateStr, origin, dest, timeStr, status, priceStr, category, airline, gate, seats});
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error reading flights: " + e.getMessage());
        }
        return list.toArray(new Object[0][]);
    }

    private static Object[][] queryBookingsFromMySQL() {
        ArrayList<Object[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT b.*, f.origin_code, f.dest_code FROM bookings b JOIN flights f ON b.flight_id = f.flight_id ORDER BY b.txn_id DESC")) {
            
            while (rs.next()) {
                String txnId = rs.getString("txn_id");
                String name = rs.getString("passenger_name");
                String flightId = rs.getString("flight_id");
                String route = rs.getString("origin_code") + " -> " + rs.getString("dest_code");
                String seat = rs.getString("seat");
                
                double price = rs.getDouble("price_paid");
                String priceStr = String.valueOf((int) price);
                String status = rs.getString("status");

                list.add(new Object[]{txnId, name, flightId, route, seat, priceStr, status});
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error reading bookings: " + e.getMessage());
        }
        return list.toArray(new Object[0][]);
    }

    private static void insertBookingToMySQL(String txnId, String name, String flightId, String seat, double price, String status) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)");
                 PreparedStatement pstmtFlight = conn.prepareStatement("UPDATE flights SET seats_available = seats_available - 1 WHERE flight_id = ?")) {
                
                pstmt.setString(1, txnId);
                pstmt.setString(2, name.toUpperCase());
                pstmt.setString(3, flightId);
                pstmt.setString(4, seat.toUpperCase());
                pstmt.setDouble(5, price);
                pstmt.setString(6, status);
                pstmt.executeUpdate();

                pstmtFlight.setString(1, flightId);
                pstmtFlight.executeUpdate();

                // Lock flight status to fully booked if 0 seats remain
                try (PreparedStatement pstmtCheck = conn.prepareStatement("UPDATE flights SET status = 'FULLY BOOKED' WHERE flight_id = ? AND seats_available <= 0")) {
                    pstmtCheck.setString(1, flightId);
                    pstmtCheck.executeUpdate();
                }

                conn.commit();
                System.out.println("✅ Confirmed transaction saved into MySQL ledger database: " + txnId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Insert booking failed, fallback active: " + e.getMessage());
        }
    }

    private static void updateBookingStatusInMySQL(String txnId, String status) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            
            String flightId = "";
            try (PreparedStatement pstmtSelect = conn.prepareStatement("SELECT flight_id FROM bookings WHERE txn_id = ?")) {
                pstmtSelect.setString(1, txnId);
                try (ResultSet rs = pstmtSelect.executeQuery()) {
                    if (rs.next()) flightId = rs.getString("flight_id");
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE bookings SET status = ? WHERE txn_id = ?");
                 PreparedStatement pstmtFlight = conn.prepareStatement("UPDATE flights SET seats_available = seats_available + 1, status = 'AVAILABLE' WHERE flight_id = ?")) {
                
                pstmt.setString(1, status);
                pstmt.setString(2, txnId);
                pstmt.executeUpdate();

                if (!flightId.isEmpty()) {
                    pstmtFlight.setString(1, flightId);
                    pstmtFlight.executeUpdate();
                }

                conn.commit();
                System.out.println("✅ Cancelled transaction processed inside MySQL database.");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Update booking status failed: " + e.getMessage());
        }
    }


    private static String getRandomStatus(Random rand) {
        int val = rand.nextInt(12);
        if (val < 7) return "AVAILABLE";
        if (val == 7 || val == 8) return "FULLY BOOKED";
        if (val == 9 || val == 10) return "DELAYED";
        return "CANCELLED";
    }

    private static String getDelayedTime(String originalTime, Random rand) {
        try {
            java.util.Date parsed = new java.text.SimpleDateFormat("h:mm a").parse(originalTime);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(parsed);
            cal.add(java.util.Calendar.HOUR_OF_DAY, 1 + rand.nextInt(3));
            cal.add(java.util.Calendar.MINUTE, rand.nextBoolean() ? 30 : 0);
            return new java.text.SimpleDateFormat("h:mm a").format(cal.getTime());
        } catch (Exception e) {
            return originalTime;
        }
    }
}
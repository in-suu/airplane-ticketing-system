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

    // Simulated Roster lists for in-memory fallback
    private static ArrayList<Object[]> simulatedFlights = new ArrayList<>();
    private static ArrayList<Object[]> simulatedBookings = new ArrayList<>();

    static {
        // Initialize dynamic simulated memory rosters in case MySQL is offline
        initializeSimulatedData();
    }

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
        if (DBConnection.isMySQLActive()) {
            return queryFlightsFromMySQL();
        } else {
            return simulatedFlights.toArray(new Object[0][]);
        }
    }

    public static Object[][] getBookingsDB() {
        if (DBConnection.isMySQLActive()) {
            return queryBookingsFromMySQL();
        } else {
            return simulatedBookings.toArray(new Object[0][]);
        }
    }

    public static void bookPassenger(String txnId, String name, String flightId, String seat, double price, String status) {
        if (DBConnection.isMySQLActive()) {
            insertBookingToMySQL(txnId, name, flightId, seat, price, status);
        } else {
            // Find flight and deduct seats
            for (Object[] f : simulatedFlights) {
                if (f[0].toString().equalsIgnoreCase(flightId)) {
                    try {
                        int currentSeats = Integer.parseInt(f[10].toString());
                        if (currentSeats > 0) {
                            f[10] = String.valueOf(currentSeats - 1);
                            if (currentSeats - 1 == 0) {
                                f[5] = "FULLY BOOKED";
                            }
                        }
                    } catch (Exception e) {}
                    break;
                }
            }
            // Fetch route
            String route = "MNL -> CEB";
            for (Object[] f : simulatedFlights) {
                if (f[0].toString().equalsIgnoreCase(flightId)) {
                    route = f[2] + " -> " + f[3];
                    break;
                }
            }
            simulatedBookings.add(new Object[]{txnId, name, flightId, route, seat, String.valueOf((int)price), status});
        }
    }

    public static void cancelBooking(String txnId) {
        if (DBConnection.isMySQLActive()) {
            updateBookingStatusInMySQL(txnId, "CANCELLED");
        } else {
            for (Object[] b : simulatedBookings) {
                if (b[0].toString().equalsIgnoreCase(txnId)) {
                    b[6] = "CANCELLED";
                    // Add seat back to flight
                    String flightId = b[2].toString();
                    for (Object[] f : simulatedFlights) {
                        if (f[0].toString().equalsIgnoreCase(flightId)) {
                            try {
                                int currentSeats = Integer.parseInt(f[10].toString());
                                f[10] = String.valueOf(currentSeats + 1);
                                if (f[5].toString().equalsIgnoreCase("FULLY BOOKED")) {
                                    f[5] = "AVAILABLE";
                                }
                            } catch (Exception e) {}
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public static ArrayList<String> getOccupiedSeats(String flightId) {
        ArrayList<String> occupied = new ArrayList<>();
        if (DBConnection.isMySQLActive()) {
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
        } else {
            for (Object[] b : simulatedBookings) {
                if (b[2].toString().equalsIgnoreCase(flightId) && !b[6].toString().equalsIgnoreCase("CANCELLED")) {
                    occupied.add(b[4].toString().toUpperCase());
                }
            }
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

    // --- IN-MEMORY INITIALIZER SIMULATOR FALLBACK ---

    private static void initializeSimulatedData() {
        String[] domestic = {"CEB", "DVO", "PPS", "MPH", "ILO", "BCD", "KLO", "TAC", "CGY", "TAG", "GES"};
        String[] international = {"NRT", "SIN", "LAX", "LHR", "HKG", "ICN", "KUL", "BKK", "SYD", "DXB"};
        
        String[] domesticAirlines = {"PAL", "Cebu Pacific", "AirAsia"};
        String[] intlAirlines = {"PAL", "Singapore Airlines", "ANA", "British Airways"};

        Random rand = new Random(42);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        Calendar cal = Calendar.getInstance();

        int flightCounter = 1;

        // Generate 3 months of flights (90 days)
        for (int day = 0; day < 90; day++) {
            Date date = cal.getTime();
            String dateStr = sdf.format(date);

            // Daily Domestic Flights
            for (String dest : domestic) {
                String flightId = "PR-DOM" + String.format("%03d", flightCounter++);
                String timeRaw = getRandomTime(rand);
                String time = "";
                try {
                    Date parsed = new SimpleDateFormat("HH:mm:ss").parse(timeRaw);
                    time = new SimpleDateFormat("h:mm a").format(parsed);
                } catch(Exception e) {
                    time = timeRaw.substring(0, 5);
                }
                String status = getRandomStatus(rand);
                double price = 2000.00 + rand.nextInt(3500);
                String priceStr = "P" + NumberFormat.getIntegerInstance().format(price);
                String airline = domesticAirlines[rand.nextInt(domesticAirlines.length)];
                String gate = "G-" + String.format("%02d", rand.nextInt(20) + 1);
                int seats = status.equals("FULLY BOOKED") ? 0 : (20 + rand.nextInt(130));

                simulatedFlights.add(new Object[]{
                    flightId, dateStr, "MNL", dest, time, status, priceStr, "domestic", airline, gate, String.valueOf(seats)
                });
            }

            // Daily International Flights
            for (String dest : international) {
                String flightId = "PR-INT" + String.format("%03d", flightCounter++);
                String timeRaw = getRandomTime(rand);
                String time = "";
                try {
                    Date parsed = new SimpleDateFormat("HH:mm:ss").parse(timeRaw);
                    time = new SimpleDateFormat("h:mm a").format(parsed);
                } catch(Exception e) {
                    time = timeRaw.substring(0, 5);
                }
                String status = getRandomStatus(rand);
                double price = 10000.00 + rand.nextInt(40000);
                String priceStr = "P" + NumberFormat.getIntegerInstance().format(price);
                String airline = intlAirlines[rand.nextInt(intlAirlines.length)];
                String gate = "T" + (rand.nextInt(3) + 1) + "-" + String.format("%03d", rand.nextInt(120) + 1);
                int seats = status.equals("FULLY BOOKED") ? 0 : (50 + rand.nextInt(200));

                simulatedFlights.add(new Object[]{
                    flightId, dateStr, "MNL", dest, time, status, priceStr, "international", airline, gate, String.valueOf(seats)
                });
            }

            cal.add(Calendar.DATE, 1);
        }

        // Initialize Booking Ledger Simulator records (Specific Passenger List)
        simulatedBookings.add(new Object[]{"TXN-10042", "AGLUBAT, JOHN GRAY D.", "PR-DOM001", "MNL -> CEB", "12A", "2500", "CONFIRMED"});
        simulatedBookings.add(new Object[]{"TXN-10043", "ALBERO, JOHN MHEL EDRIAN D.", "PR-DOM002", "MNL -> DVO", "04C", "3200", "CONFIRMED"});
        simulatedBookings.add(new Object[]{"TXN-10044", "ALCOVENDAS, ALLYZA JOY M.", "PR-INT002", "MNL -> SIN", "18F", "12200", "CONFIRMED"});
        simulatedBookings.add(new Object[]{"TXN-10045", "AMIDO, AMIEL R.", "PR-DOM003", "MNL -> PPS", "21B", "2100", "CANCELLED"});
        simulatedBookings.add(new Object[]{"TXN-10046", "AMORES, SOPHIA KATE D.", "PR-INT001", "MNL -> NRT", "02A", "18500", "CONFIRMED"});

        // Auto-occupy some seats dynamically in simulator too!
        int simulatedTxnId = 10047;
        String[] passengerNames = {
            "AGLUBAT, JOHN GRAY D.", "ALBERO, JOHN MHEL EDRIAN D.", "ALCOVENDAS, ALLYZA JOY M.",
            "AMIDO, AMIEL R.", "AMORES, SOPHIA KATE D.", "ANAJAO, DAREL C.",
            "ANCHETA, REYNALDO JR. M.", "APARECE, HARRY D.", "BAJIO, KAIZEN PETER U.",
            "BALTAZAR, BIANCA DAVE N.", "BAUTISTA, CHLOE G.", "BAUTISTA, MAN JELORD A.",
            "BERMEJO, RAYVER DANE S.", "BERMUNDO, JOHN ANDRE M.", "BISMONTE, LEONARDO B.",
            "BONGCAYAO, JEAN AARON J.", "BUENA, JOHN PATRICK C.", "CANAPI, JUMEL CEDRIC G.",
            "CANTONES, CHARMEE JOY P.", "CARLOS, RAFAEL III M.", "COSING, YUN-TZU C.",
            "DARNAYLA, ADIAN KRYLE P.", "DORONILA, CHARLIE MONIQUE R.", "ELEPTICO, JARED ANTHONY V.",
            "ESPERIDA, JUSTINE H.", "FABILLAR, JOSH HENRICH M.", "GABUTIN, LEVIN TRISTRAM P.",
            "LUMABI, JAYVEE B.", "MALDO, KACEY BJORN M.", "MANALO, CYRIL C.",
            "MANUEL, ALTHEA NICOLE B.", "ORIAS, CLIVE KIROS A.", "OROPILLA, RALPH EIVAN P.",
            "PINEDA, ZANJOE E.", "ROLDAN, JAYDEL JADE C.", "SANTIAGO, ABRAHAM B.",
            "SIBLERO, YSAMARIE T.", "SILVANO, RANEL M.", "TADONG, RADLEY CYNRIC E.",
            "TAGUNICAR, ETHAN HEINRICH R.", "TAN, JUSTIN H.", "TOGONON, LYAN JOHN L.",
            "VILLARTA, SCOTT ANDREI R."
        };
        for (Object[] f : simulatedFlights) {
            String flightId = f[0].toString();
            String status = f[5].toString();
            String route = f[2] + " -> " + f[3];
            String priceStr = f[6].toString().replace("P", "").replace(",", "");
            if (status.equals("AVAILABLE")) {
                int count = rand.nextInt(5) + 3; // 3 to 7 occupied seats
                java.util.HashSet<String> used = new java.util.HashSet<>();
                for (int i = 0; i < count; i++) {
                    String seatLetter = String.valueOf((char)('A' + rand.nextInt(6)));
                    int seatRow = rand.nextInt(20) + 1;
                    String seatStr = String.format("%02d%s", seatRow, seatLetter);
                    if (!used.contains(seatStr)) {
                        used.add(seatStr);
                        String tId = "TXN-" + simulatedTxnId++;
                        String pName = passengerNames[rand.nextInt(passengerNames.length)];
                        simulatedBookings.add(new Object[]{tId, pName, flightId, route, seatStr, priceStr, "CONFIRMED"});
                    }
                }
            }
        }
    }

    private static String getRandomTime(Random rand) {
        String[] times = {"06:15:00", "08:00:00", "09:40:00", "10:30:00", "13:00:00", "15:45:00", "18:20:00", "22:10:00", "23:45:00"};
        return times[rand.nextInt(times.length)];
    }

    private static String getRandomStatus(Random rand) {
        int val = rand.nextInt(10);
        if (val < 8) return "AVAILABLE";
        if (val == 8) return "FULLY BOOKED";
        return "DELAYED";
    }
}
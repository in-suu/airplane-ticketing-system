package com.system.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DBConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/byaheng_langit?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = ""; // Default local MySQL blank password on Windows

    private static boolean useMySQL = false;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            // Attempt to connect to server and build schema
            try (Connection conn = DriverManager.getConnection(DB_SERVER_URL, USER, PASS);
                 Statement stmt = conn.createStatement()) {
                
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS byaheng_langit");
                useMySQL = true;
                System.out.println("🎉 Successfully connected to Local MySQL Server! Active database: byaheng_langit");
            }
        } catch (Exception e) {
            System.out.println("⚠️ MySQL Server is offline or not installed. Gracefully falling back to high-fidelity In-Memory Database Simulator.");
            useMySQL = false;
        }

        // Initialize schema and populate if using MySQL
        if (useMySQL) {
            initializeDatabaseSchema();
        }
    }

    public static boolean isMySQLActive() {
        return useMySQL;
    }

    public static Connection getConnection() {
        if (!useMySQL) return null;
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to acquire active connection, falling back to simulator: " + e.getMessage());
            return null;
        }
    }

    private static void initializeDatabaseSchema() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn == null) return;

            // 1. Create airports table (Normalized)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS airports ("
                    + "airport_code VARCHAR(10) PRIMARY KEY,"
                    + "full_name VARCHAR(100) NOT NULL,"
                    + "city VARCHAR(50) NOT NULL,"
                    + "country VARCHAR(50) NOT NULL"
                    + ")");

            // 2. Create flights table (Normalized with Foreign Keys)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS flights ("
                    + "flight_id VARCHAR(20) PRIMARY KEY,"
                    + "origin_code VARCHAR(10) NOT NULL,"
                    + "dest_code VARCHAR(10) NOT NULL,"
                    + "departure_date DATE NOT NULL,"
                    + "departure_time TIME NOT NULL,"
                    + "status VARCHAR(20) NOT NULL,"
                    + "price DECIMAL(10,2) NOT NULL,"
                    + "category VARCHAR(20) NOT NULL,"
                    + "airline VARCHAR(50) NOT NULL,"
                    + "gate VARCHAR(20) NOT NULL,"
                    + "seats_available INT NOT NULL,"
                    + "FOREIGN KEY (origin_code) REFERENCES airports(airport_code),"
                    + "FOREIGN KEY (dest_code) REFERENCES airports(airport_code)"
                    + ")");

            // 3. Create bookings table (Normalized with Foreign Keys)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings ("
                    + "txn_id VARCHAR(20) PRIMARY KEY,"
                    + "passenger_name VARCHAR(100) NOT NULL,"
                    + "flight_id VARCHAR(20) NOT NULL,"
                    + "seat VARCHAR(10) NOT NULL,"
                    + "price_paid DECIMAL(10,2) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL,"
                    + "FOREIGN KEY (flight_id) REFERENCES flights(flight_id)"
                    + ")");

            // Check if airports populated, if not fill them
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM airports");
            if (rs.next() && rs.getInt(1) == 0) {
                populateAirports(stmt);
            }

            // Check if flights populated, if not generate 3-month dynamic flights schedule
            rs = stmt.executeQuery("SELECT COUNT(*) FROM flights");
            if (rs.next() && rs.getInt(1) == 0) {
                populateFlightsSchedule(conn);
            }
            
            // Check if bookings populated, if not populate standard baseline records
            rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings");
            if (rs.next() && rs.getInt(1) == 0) {
                populateDefaultBookings(stmt);
            }

        } catch (Exception e) {
            System.err.println("❌ Database schema initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void populateAirports(Statement stmt) throws Exception {
        String[] queries = {
            "INSERT INTO airports VALUES ('MNL', 'Byaheng Langit International Airport', 'Manila', 'Philippines')",
            "INSERT INTO airports VALUES ('CEB', 'Mactan-Cebu International Airport', 'Cebu', 'Philippines')",
            "INSERT INTO airports VALUES ('DVO', 'Francisco Bangoy International Airport', 'Davao', 'Philippines')",
            "INSERT INTO airports VALUES ('PPS', 'Puerto Princesa International Airport', 'Puerto Princesa', 'Philippines')",
            "INSERT INTO airports VALUES ('MPH', 'Caticlan Airport', 'Boracay', 'Philippines')",
            "INSERT INTO airports VALUES ('ILO', 'Iloilo International Airport', 'Iloilo', 'Philippines')",
            "INSERT INTO airports VALUES ('BCD', 'Bacolod-Silay Airport', 'Bacolod', 'Philippines')",
            "INSERT INTO airports VALUES ('KLO', 'Kalibo International Airport', 'Kalibo', 'Philippines')",
            "INSERT INTO airports VALUES ('TAC', 'Daniel Z. Romualdez Airport', 'Tacloban', 'Philippines')",
            "INSERT INTO airports VALUES ('CGY', 'Laguindingan Airport', 'Cagayan de Oro', 'Philippines')",
            "INSERT INTO airports VALUES ('TAG', 'Bohol-Panglao International Airport', 'Tagbilaran', 'Philippines')",
            "INSERT INTO airports VALUES ('GES', 'General Santos International Airport', 'General Santos', 'Philippines')",
            "INSERT INTO airports VALUES ('NRT', 'Narita International Airport', 'Tokyo', 'Japan')",
            "INSERT INTO airports VALUES ('SIN', 'Changi International Airport', 'Singapore', 'Singapore')",
            "INSERT INTO airports VALUES ('LAX', 'Los Angeles International Airport', 'Los Angeles', 'United States')",
            "INSERT INTO airports VALUES ('LHR', 'London Heathrow Airport', 'London', 'United Kingdom')",
            "INSERT INTO airports VALUES ('HKG', 'Hong Kong International Airport', 'Hong Kong', 'Hong Kong')",
            "INSERT INTO airports VALUES ('ICN', 'Incheon International Airport', 'Seoul', 'South Korea')",
            "INSERT INTO airports VALUES ('KUL', 'Kuala Lumpur International Airport', 'Kuala Lumpur', 'Malaysia')",
            "INSERT INTO airports VALUES ('BKK', 'Suvarnabhumi Airport', 'Bangkok', 'Thailand')",
            "INSERT INTO airports VALUES ('SYD', 'Sydney Airport', 'Sydney', 'Australia')",
            "INSERT INTO airports VALUES ('DXB', 'Dubai International Airport', 'Dubai', 'United Arab Emirates')"
        };
        for (String q : queries) {
            stmt.executeUpdate(q);
        }
        System.out.println("✅ Populated airports lookup database table.");
    }

    private static void populateFlightsSchedule(Connection conn) throws Exception {
        String sql = "INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlBook = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)";
        
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
        java.sql.PreparedStatement pstmtBook = conn.prepareStatement(sqlBook);

        String[] domestic = {"CEB", "DVO", "PPS", "MPH", "ILO", "BCD", "KLO", "TAC", "CGY", "TAG", "GES"};
        String[] international = {"NRT", "SIN", "LAX", "LHR", "HKG", "ICN", "KUL", "BKK", "SYD", "DXB"};
        
        String[] domesticAirlines = {"PAL", "Cebu Pacific", "AirAsia"};
        String[] intlAirlines = {"PAL", "Singapore Airlines", "ANA", "British Airways"};
        
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

        Random rand = new Random(42); // Seeded for consistency
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        int flightCounter = 1;
        int txnCounter = 10047;

        // Generate 3 months of flights (90 days)
        for (int day = 0; day < 90; day++) {
            Date date = cal.getTime();
            String dateStr = sdf.format(date);

            // Daily Domestic Flights
            for (String dest : domestic) {
                String flightId = "PR-DOM" + String.format("%03d", flightCounter++);
                String time = getRandomTime(rand);
                String status = getRandomStatus(rand);
                double price = 2000.00 + rand.nextInt(3500);
                String airline = domesticAirlines[rand.nextInt(domesticAirlines.length)];
                String gate = "G-" + String.format("%02d", rand.nextInt(20) + 1);
                int seats = status.equals("FULLY BOOKED") ? 0 : (20 + rand.nextInt(130));

                pstmt.setString(1, flightId);
                pstmt.setString(2, "MNL");
                pstmt.setString(3, dest);
                pstmt.setString(4, dateStr);
                pstmt.setString(5, time);
                pstmt.setString(6, status);
                pstmt.setDouble(7, price);
                pstmt.setString(8, "domestic");
                pstmt.setString(9, airline);
                pstmt.setString(10, gate);
                pstmt.setInt(11, seats);
                pstmt.addBatch();

                // If flight is AVAILABLE, populate some randomly occupied seats!
                if (status.equals("AVAILABLE")) {
                    int numBookings = rand.nextInt(6) + 3; // 3 to 8 pre-occupied seats
                    java.util.HashSet<String> usedSeats = new java.util.HashSet<>();
                    for (int k = 0; k < numBookings; k++) {
                        String seatLetter = String.valueOf((char)('A' + rand.nextInt(6))); // A to F
                        int seatRow = rand.nextInt(20) + 1;
                        String seatStr = String.format("%02d%s", seatRow, seatLetter);
                        if (!usedSeats.contains(seatStr)) {
                            usedSeats.add(seatStr);
                            String txnId = "TXN-" + txnCounter++;
                            String passengerName = passengerNames[rand.nextInt(passengerNames.length)];
                            
                            pstmtBook.setString(1, txnId);
                            pstmtBook.setString(2, passengerName);
                            pstmtBook.setString(3, flightId);
                            pstmtBook.setString(4, seatStr);
                            pstmtBook.setDouble(5, price);
                            pstmtBook.setString(6, "CONFIRMED");
                            pstmtBook.addBatch();
                        }
                    }
                }
            }

            // Daily International Flights
            for (String dest : international) {
                String flightId = "PR-INT" + String.format("%03d", flightCounter++);
                String time = getRandomTime(rand);
                String status = getRandomStatus(rand);
                double price = 10000.00 + rand.nextInt(40000);
                String airline = intlAirlines[rand.nextInt(intlAirlines.length)];
                String gate = "T" + (rand.nextInt(3) + 1) + "-" + String.format("%03d", rand.nextInt(120) + 1);
                int seats = status.equals("FULLY BOOKED") ? 0 : (50 + rand.nextInt(200));

                pstmt.setString(1, flightId);
                pstmt.setString(2, "MNL");
                pstmt.setString(3, dest);
                pstmt.setString(4, dateStr);
                pstmt.setString(5, time);
                pstmt.setString(6, status);
                pstmt.setDouble(7, price);
                pstmt.setString(8, "international");
                pstmt.setString(9, airline);
                pstmt.setString(10, gate);
                pstmt.setInt(11, seats);
                pstmt.addBatch();

                // If flight is AVAILABLE, populate some randomly occupied seats!
                if (status.equals("AVAILABLE")) {
                    int numBookings = rand.nextInt(8) + 4; // 4 to 11 pre-occupied seats
                    java.util.HashSet<String> usedSeats = new java.util.HashSet<>();
                    for (int k = 0; k < numBookings; k++) {
                        String seatLetter = String.valueOf((char)('A' + rand.nextInt(8))); // A to H
                        int seatRow = rand.nextInt(35) + 1;
                        String seatStr = String.format("%02d%s", seatRow, seatLetter);
                        if (!usedSeats.contains(seatStr)) {
                            usedSeats.add(seatStr);
                            String txnId = "TXN-" + txnCounter++;
                            String passengerName = passengerNames[rand.nextInt(passengerNames.length)];
                            
                            pstmtBook.setString(1, txnId);
                            pstmtBook.setString(2, passengerName);
                            pstmtBook.setString(3, flightId);
                            pstmtBook.setString(4, seatStr);
                            pstmtBook.setDouble(5, price);
                            pstmtBook.setString(6, "CONFIRMED");
                            pstmtBook.addBatch();
                        }
                    }
                }
            }

            cal.add(Calendar.DATE, 1);
        }

        pstmt.executeBatch();
        pstmtBook.executeBatch();
        pstmt.close();
        pstmtBook.close();
        System.out.println("✅ Pre-populated 3 months flight roster batches successfully in database.");
    }

    private static void populateDefaultBookings(Statement stmt) throws Exception {
        String[] queries = {
            "INSERT INTO bookings VALUES ('TXN-10042', 'YUN-TZU COSING', 'PR-DOM001', '12A', 2500.00, 'CONFIRMED')",
            "INSERT INTO bookings VALUES ('TXN-10043', 'ANDRE SANTOS', 'PR-DOM002', '04C', 3200.00, 'CONFIRMED')",
            "INSERT INTO bookings VALUES ('TXN-10044', 'SHAUN DE SILVA', 'PR-INT002', '18F', 12200.00, 'CONFIRMED')",
            "INSERT INTO bookings VALUES ('TXN-10045', 'JEANE MALABO', 'PR-DOM003', '21B', 2100.00, 'CANCELLED')",
            "INSERT INTO bookings VALUES ('TXN-10046', 'CZY MENDOZA', 'PR-INT001', '02A', 18500.00, 'CONFIRMED')"
        };
        for (String q : queries) {
            stmt.executeUpdate(q);
        }
        System.out.println("✅ Populated initial booking records table.");
    }

    private static String getRandomTime(Random rand) {
        String[] times = {"06:15:00", "08:00:00", "09:40:00", "10:30:00", "13:00:00", "15:45:00", "18:20:00", "22:10:00", "23:45:00"};
        return times[rand.nextInt(times.length)];
    }

    private static String getRandomStatus(Random rand) {
        // 80% Available, 10% Fully Booked, 10% Delayed
        int val = rand.nextInt(10);
        if (val < 8) return "AVAILABLE";
        if (val == 8) return "FULLY BOOKED";
        return "DELAYED";
    }
}

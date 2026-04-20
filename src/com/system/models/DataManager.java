package com.system.models;
import java.awt.*;
import java.util.ArrayList;

public class DataManager {
    // Shared List for Bookings (Used by Member 4 & 5 later)
    public static ArrayList<Passenger> currentBookings = new ArrayList<>();
    public static Object[] selectedFlightData;
    
    // UI Branding Constants
    public static final Color MIDNIGHT_BLUE = new Color(0, 51, 102);
    public static final Color SUNSET_ORANGE = new Color(255, 87, 34);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 22);
}


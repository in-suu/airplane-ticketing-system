package com.system.models;

import java.awt.Color;
import java.util.ArrayList;

public class DataManager {
    public static final Color SUNSET_ORANGE = new Color(255, 140, 0);

    public static Object[] selectedFlightData; 
    public static ArrayList<Passenger> bookedPassengers = new ArrayList<>();
    
    public static Object[][] getMockFlightsDB() {
        return new Object[][] {
            // ID(0), Date(1), Origin(2), Dest(3), Time(4), Status(5), Price(6), Cat(7), Airline(8), Gate(9), Seats(10)
            {"PH-DOM1", "05-04-26", "MNL", "CEB", "08:00", "AVAILABLE", "P2,500", "domestic", "PAL", "G-12", "45"},
            {"PH-DOM2", "05-04-26", "MNL", "DVO", "10:30", "AVAILABLE", "P3,200", "domestic", "Cebu Pacific", "G-08", "12"},
            {"PH-DOM3", "05-04-26", "MNL", "PPS", "13:00", "FULLY BOOKED", "P2,800", "domestic", "AirAsia", "G-05", "0"},
            {"PH-DOM4", "05-04-26", "MNL", "MPH", "15:45", "DELAYED", "P4,500", "domestic", "PAL", "G-14", "20"},
            {"PH-DOM5", "05-04-26", "MNL", "ILO", "18:20", "CANCELLED", "P2,100", "domestic", "Cebu Pacific", "G-01", "0"},
            {"PH-INT1", "06-04-26", "MNL", "NRT", "06:15", "AVAILABLE", "P18,500", "international", "ANA", "T3-112", "150"},
            {"PH-INT2", "06-04-26", "MNL", "SIN", "09:40", "DELAYED", "P12,200", "international", "Singapore Airlines", "T3-115", "85"},
            {"PH-INT3", "07-04-26", "MNL", "LAX", "22:10", "AVAILABLE", "P38,000", "international", "PAL", "T1-04", "40"},
            {"PH-INT5", "08-04-26", "MNL", "LHR", "23:45", "FULLY BOOKED", "P45,000", "international", "British Airways", "T1-08", "0"}
        };
    }

    public static ArrayList<Object[]> bookingRecords = new ArrayList<>();
    
    static {
        bookingRecords.add(new Object[]{"TXN-10042", "Yun-Tzu Cosing", "PH-DOM1", "MNL -> CEB", "12A", "2500", "CONFIRMED"});
        bookingRecords.add(new Object[]{"TXN-10043", "Andre Santos", "PH-DOM2", "MNL -> DVO", "04C", "3200", "CONFIRMED"});
        bookingRecords.add(new Object[]{"TXN-10044", "Shaun De Silva", "PH-INT2", "MNL -> SIN", "18F", "12200", "CONFIRMED"});
        bookingRecords.add(new Object[]{"TXN-10045", "Jeane Malabo", "PH-DOM5", "MNL -> ILO", "21B", "2100", "CANCELLED"});
        bookingRecords.add(new Object[]{"TXN-10046", "Czy Mendoza", "PH-INT1", "MNL -> NRT", "02A", "18500", "CONFIRMED"});
    }

    public static Object[][] getMockRecordsDB() {
        return bookingRecords.toArray(new Object[0][]);
    }
}
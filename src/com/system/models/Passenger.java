package com.system.models;

public class Passenger {
    // Encapsulation: private variables
    private String name;
    private int age;
    private String seatNumber;
    private double baggageWeight;
    private boolean hasInsurance;

    // Constructor: Para madaling gawan ng object mamaya
    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters: Para ma-access ang data nang safe
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public double getBaggageWeight() { return baggageWeight; }
    public void setBaggageWeight(double baggageWeight) { this.baggageWeight = baggageWeight; }

    public boolean isHasInsurance() { return hasInsurance; }
    public void setHasInsurance(boolean hasInsurance) { this.hasInsurance = hasInsurance; }
}
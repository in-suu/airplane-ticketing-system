package com.system.models;

public class Passenger {
    private String name;
    private String contact;
    private String passport;
    private int age;

    // Constructor
    public Passenger(String name, String contact, String passport, int age) {
        this.name = name;
        this.contact = contact;
        this.passport = passport;
        this.age = age;
    }

    // --- GETTERS AND SETTERS ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getPassport() { return passport; }
    public void setPassport(String passport) { this.passport = passport; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
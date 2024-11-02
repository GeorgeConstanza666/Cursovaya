package com.example.kursovaya;

public class Driver {
    private int driverID;
    private String name;
    private String licenseNumber;
    private String phoneNumber;

    public Driver(int driverID, String name, String licenseNumber, String phoneNumber) {
        this.driverID = driverID;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phoneNumber = phoneNumber;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

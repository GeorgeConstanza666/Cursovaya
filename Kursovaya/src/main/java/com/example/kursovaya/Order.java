package com.example.kursovaya;

public class Order {
    private int orderID;
    private String pickupLocation;
    private String dropoffLocation;
    private double distance;
    private int numberOfPassengers;
    private int driverID;
    private int clientID;

    public Order(int orderID, String pickupLocation, String dropoffLocation, double distance, int numberOfPassengers, int driverID, int clientID) {
        this.orderID = orderID;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.distance = distance;
        this.numberOfPassengers = numberOfPassengers;
        this.driverID = driverID;
        this.clientID = clientID;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public double getDistance() {
        return distance;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public int getDriverID() {
        return driverID;
    }

    public int getClientID() {
        return clientID;
    }
}

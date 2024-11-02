package com.example.kursovaya;

public class Receipt {
    private String pickupLocation;
    private String dropoffLocation;
    private double distance;
    private int numberOfPassengers;
    private String orderTime;
    private double estimatedFare;

    public Receipt(String pickupLocation, String dropoffLocation, double distance, int numberOfPassengers, String orderTime, double estimatedFare) {
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.distance = distance;
        this.numberOfPassengers = numberOfPassengers;
        this.orderTime = orderTime;
        this.estimatedFare = estimatedFare;
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

    public String getOrderTime() {
        return orderTime;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }
}

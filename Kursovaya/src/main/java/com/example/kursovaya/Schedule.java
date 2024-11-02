package com.example.kursovaya;

public class Schedule {
    private int ShiftID;
    private int driverID;
    private int vehicleID;
    private String startTime;
    private String endTime;
    private String status;

    public Schedule(int ShiftID, int driverID, int vehicleID, String startTime, String endTime, String status) {
        this.ShiftID = ShiftID;
        this.driverID = driverID;
        this.vehicleID = vehicleID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getShiftID() {
        return ShiftID;
    }

    public void setShiftID(int shiftID) {
        this.ShiftID = shiftID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

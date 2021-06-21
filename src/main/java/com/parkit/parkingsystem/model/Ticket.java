package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * @author Ridouan
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private Date inTime;
    private Date outTime;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        if (inTime == null) {
            return null;
        }
        return new Date(inTime.getTime());
    }

    public void setInTime(Date inTime) {
        if (inTime == null) {
            this.inTime = null;
        } else {
            this.inTime = new Date(inTime.getTime());
        }
    }


    public Date getOutTime() {
        if (outTime == null) {
         return null;
        }
        return new Date(outTime.getTime());
    }

    public void setOutTime(Date outTime) {
        if (outTime == null) {
            this.outTime = null;
        } else {
            this.outTime = new Date(outTime.getTime());
        }
    }



}

package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket,boolean usualVehicle ){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime= ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();
        long duration  = Math.max(outTime- inTime - (30*60*1000) , 0);
        double durationH = (duration/1000/60)/60.0;
        double usualFare = usualVehicle ? 0.95 : 1.0;
        switch (ticket.getParkingSpot(true).getParkingType()){
            case CAR: {
                ticket.setPrice(durationH * Fare.CAR_RATE_PER_HOUR * usualFare);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationH * Fare.BIKE_RATE_PER_HOUR * usualFare );

                break;
            }


            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
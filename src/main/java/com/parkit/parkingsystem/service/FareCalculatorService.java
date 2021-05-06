package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket,boolean usualVehicule ){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime= ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();
        long duration  = Math.max(outTime- inTime - (30*60*1000) , 0);
        double durationH = (duration/1000/60)/60.0;
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationH * Fare.CAR_RATE_PER_HOUR );
                break;
            }
            case BIKE: {
                ticket.setPrice(durationH * Fare.BIKE_RATE_PER_HOUR );

                break;
            }
            //case usualVehicle ajouter la fonction pour calculer -5%

            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
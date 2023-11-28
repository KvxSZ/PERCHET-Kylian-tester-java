package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }

    public void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect");
        }

        int inHour = (int) ticket.getInTime().getTime();
        int outHour = (int) ticket.getOutTime().getTime();

        long durationInMinutes = TimeUnit.MINUTES.convert(outHour - inHour, TimeUnit.MILLISECONDS);
        double durationInHours =  durationInMinutes / 60D;

        if (durationInMinutes < 30) {
            ticket.setPrice(0.0);
        } else {

            double discountFactor = discount ? 0.95 : 1D;

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR * discountFactor);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR * discountFactor);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}
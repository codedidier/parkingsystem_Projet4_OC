package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public double calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
//todo#1 : Corriger le code pour faire passer les tests unitaires
        // int inHour = ticket.getInTime().getHours();
        // int outHour = ticket.getOutTime().getHours();

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        // TODO: Some tests are failing here. Need to check if this logic is correct
        // changement de int en long, et getHours" modifié en "getTime" qui n'est pas
        // obsolète et calcule également en minutes
        long duration = ((outHour - inHour) / 3600000);

        // story#1: parking gratuit -30 minutes
        if (duration <= 0.5)
            duration = (long) 0.0;

        switch (ticket.getParkingSpot().getParkingType()) {
        case CAR: {
            ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
            break;

        }
        case BIKE: {
            ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
            break;
        }
        default:
            throw new IllegalArgumentException("Unkown Parking Type");
        }
//story#2: remise 5% utilisateurs recurents

        if (ticket.isRecurringUser()) {
            ticket.setPrice(ticket.getPrice() * 0.95);
        }
        return duration;

    }

}
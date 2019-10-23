package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FlightRepository flightRepository;

    //@Autowired
    //PassengerRepository passengerRepository;

    //This gets the per passenger price for one flight leg.
    public double getPricePerPassenger(String flightClass, double basePrice){
        double pricePerPassenger;
        if(flightClass.equalsIgnoreCase("economy")){
            pricePerPassenger = basePrice;
        }else if (flightClass.equalsIgnoreCase("business")){
            pricePerPassenger = 2 * basePrice;
        }else{
            pricePerPassenger = 3 * basePrice;
        }
        return pricePerPassenger;
    }

    //This gets the total price for the reservation (round trip if applicable, for all passengers).
    //This assumes the return flight is the same price as the departure flight.
    public double getTotalTripPrice(boolean isRoundTrip, String flightClass,
                                    double basePrice, int numberPassengers){
        int multiplier;
        if (isRoundTrip){
            multiplier = 2;
        }else{
            multiplier=1;
        }
        double totalTripPrice = multiplier * getPricePerPassenger(flightClass,basePrice) * numberPassengers;
        return totalTripPrice;
    }
}

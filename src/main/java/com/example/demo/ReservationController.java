package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;


    @GetMapping("/flightsearchform")
    public String showFlightSearchForm(Model model){
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("flight", new Flight());
        ArrayList<Flight> flights = flightRepository.findAll();
        Flight flight = new Flight();
        Set<String> airports = flight.getAirportList(flights);
        model.addAttribute("airports", airports);
        return "flightsearchform";
    }

    @PostMapping("/processflightsearch")
    public String processFlightSearch(Model model,
                                      @RequestParam(name="numberPassengers") int numPass,
                                      @RequestParam(name="SearchSelectorRT") String rtrip,
                                      @RequestParam(name="SearchSelectorPassClass") String passClass,
                                      @RequestParam(name = "SearchSelectorDepApt") String depApt,
                                      @RequestParam(name = "SearchSelectorArrApt") String arrApt,
                                      @RequestParam(name = "depDate") String depDate,
                                      @RequestParam(name = "retDate") String retDate) {
        model.addAttribute("depFlights", flightRepository
                .findByDepartureAirportContainingAndArrivalAirportContaining(depApt, arrApt));
        model.addAttribute("retFlights", flightRepository
                .findByDepartureAirportContainingAndArrivalAirportContaining(arrApt, depApt));

        boolean isRoundTrip;
        if (rtrip.equals("RoundTrip")) {
            isRoundTrip = true;
        } else {
            isRoundTrip = false;
        }
        model.addAttribute("isRoundTrip", isRoundTrip);

        String pattern = "yyyy-MM-dd";
        try {
            String formattedDepDate = depDate.substring(1);
            SimpleDateFormat simpleDepDateFormat = new SimpleDateFormat(pattern);
            Date realDepDate = simpleDepDateFormat.parse(formattedDepDate);
            model.addAttribute("depDate", realDepDate);
        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        try {
            String formattedRetDate = retDate.substring(1);
            SimpleDateFormat simpleRetDateFormat = new SimpleDateFormat(pattern);
            Date realRetDate = simpleRetDateFormat.parse(formattedRetDate);
            model.addAttribute("retDate", realRetDate);
        }
        catch (java.text.ParseException e){
            e.printStackTrace();
        }

        model.addAttribute("numPass", numPass);
        model.addAttribute("passClass", passClass);
        model.addAttribute("depApt", depApt);
        model.addAttribute("arrApt", arrApt);

        return "listSearchResults";
    }

    public double getTotalTripPrice(Reservation reservation){
        Flight departureFlight = reservation.getDepartureFlight();
        double pricePerPassDep = departureFlight.getPricePerPassenger(reservation.getFlightClass(),departureFlight.getBasePrice());
        double windowPrice = 5.00;
        int numPass = reservation.getNumberPassengers();
        double totalTripPrice = pricePerPassDep * numPass;
        if (reservation.isRoundTrip()) {
            Flight returnFlight = reservation.getArrivalFlight();
            double pricePerPassRet = returnFlight.getPricePerPassenger(reservation.getFlightClass(), returnFlight.getBasePrice());
            windowPrice = 10.00;
            totalTripPrice += pricePerPassRet * numPass;
        }
        Set<Passenger> passengers = reservation.getPassengers();
        for(Passenger passenger : passengers){
            if(passenger.getIsWindow()==true){
                totalTripPrice += windowPrice;
            }
        }
        return totalTripPrice;
    }

}

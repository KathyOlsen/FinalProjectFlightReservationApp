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

    // moved the following two methods to Flight.java

//    //This gets the per passenger price for one flight leg.
//    public double getPricePerPassenger(String flightClass, double basePrice){
//        double pricePerPassenger;
//        if(flightClass.equalsIgnoreCase("economy")){
//            pricePerPassenger = basePrice;
//        }else if (flightClass.equalsIgnoreCase("business")){
//            pricePerPassenger = 2 * basePrice;
//        }else{
//            pricePerPassenger = 3 * basePrice;
//        }
//        return pricePerPassenger;
//    }

//    //This gets the total price for the reservation (round trip if applicable, for all passengers).
//    //This assumes the return flight is the same price as the departure flight.
//    public double getTotalTripPrice(boolean isRoundTrip, String flightClass,
//                                    double basePrice, int numberPassengers){
//        int multiplier;
//        if (isRoundTrip){
//            multiplier = 2;
//        }else{
//            multiplier=1;
//        }
//        double totalTripPrice = multiplier * getPricePerPassenger(flightClass,basePrice) * numberPassengers;
//        return totalTripPrice;
//    }

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
    public String processFlightSearch(@ModelAttribute("reservation") Reservation reservation, Model model,
                                      @RequestParam(name="numberPassengers") int numPass,
                                      @RequestParam(name="SearchSelectorRT") String rtrip,
                                      @RequestParam(name="SearchSelectorPassClass") String passClass,
                                      @RequestParam(name = "SearchSelectorDepApt") String depApt,
                                      @RequestParam(name = "SearchSelectorArrApt") String arrApt
                                      /*@RequestParam(name = "depDate") String depDate,
                                      @RequestParam(name = "retDate") String retDate*/) {
        model.addAttribute("depFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(depApt, arrApt));
        model.addAttribute("retFlights", flightRepository
                .findByDepartureAirportContainingIgnoreCaseAndArrivalAirportContainingIgnoreCase(arrApt, depApt));

//        boolean isRoundTrip;
        if (rtrip.equals("RoundTrip")) {
//            isRoundTrip = true;
            reservation.setRoundTrip(true);

        } else {
//            isRoundTrip = false;
            reservation.setRoundTrip(false);
        }
//        model.addAttribute("isRoundTrip", isRoundTrip);

/*
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
*/
        reservation.setNumberPassengers(numPass);
        reservation.setFlightClass(passClass);
        model.addAttribute(reservation);

/*        model.addAttribute("numPass", numPass);
        model.addAttribute("passClass", passClass);
        model.addAttribute("depApt", depApt);
        model.addAttribute("arrApt", arrApt);*/

        return "redirect:/listSearchResults";
    }

    @GetMapping("/listSearchResults")
    public String showSearchResultsForm(@ModelAttribute("reservation"),
                                        @ModelAttribute("depFlights"),
                                        @ModelAttribute("retFlights"), Model model){

//        model.addAttribute("depFlights", depFlights);

        System.out.println(dep);
        if (reservation.isRoundTrip() == true)
            model.addAttribute("retFlights", retFlights);

        Set<Passenger> passengers = new HashSet<Passenger>();
        for (int i = 0; i < reservation.getNumberPassengers(); i++) {
            passengers.add(new Passenger());
        }
        reservation.setPassengers(passengers);
        model.addAttribute("reservation", reservation);

        return "listsearchresults";
    }


}

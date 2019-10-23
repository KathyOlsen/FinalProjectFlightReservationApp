package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Date;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    ArrayList<Reservation> findAll();
    ArrayList<Reservation> findByUserContaining(String username);
    ArrayList<Reservation> findByUserContainingAndDepartureFlightContaining(String username, String departureAirport);
    ArrayList<Reservation> findByUserContainingAndDepartureDateIsBefore (String username, Date date);
}

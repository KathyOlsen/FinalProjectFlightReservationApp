package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Date;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    ArrayList<Reservation> findAll();
    ArrayList<Reservation> findByUser(User user);
    ArrayList<Reservation> findByUserAndDepartureDateIsBefore(User user, Date date);
    //ArrayList<Reservation> findByUserAndDepartureDateIsNotBefore(User user, Date date);
    //ArrayList<Reservation> findByUserAndDepartureFlightContaining(User user, String departureAirport);
}

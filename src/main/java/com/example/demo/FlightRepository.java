package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface FlightRepository extends CrudRepository<Flight, Long> {
    ArrayList<Flight> findAll();
    ArrayList<Flight> findByDepartureAirportContainingAndArrivalAirportContaining(String airport1, String airport2);
}

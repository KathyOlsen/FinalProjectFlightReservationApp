package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    ArrayList<Reservation> findAll();
}

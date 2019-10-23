package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;

public interface PassengerRepository extends CrudRepository<Passenger, Long> {
    ArrayList<Passenger> findAll();
}

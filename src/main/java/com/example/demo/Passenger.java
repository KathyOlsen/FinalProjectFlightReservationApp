package com.example.demo;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Passenger_Data")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long reservation_id;

    private String firstName;

    private String lastName;
}

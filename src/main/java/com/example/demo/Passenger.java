package com.example.demo;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "first_name", nullable = false)
    private String firstName;

    @Column (name = "last_name", nullable = false)
    private String lastName;

    @Column (name = "seat_number", nullable = false)
    private String seatNumber;

    @Column (name = "is_window")
    private boolean isWindow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "reservation_id")
    private Reservation reservation;

    public Passenger() {
        this.isWindow = false;          // Starting out with the the isWindow = false.  The user can choose true.
    }

    public Passenger(String firstName, String lastName, String seatNumber) {
        this();                         // Picks up the isWindow = false in the null constructor.
        this.firstName = firstName;
        this.lastName = lastName;
        this.seatNumber = seatNumber;
    }

    public Passenger(String firstName, String lastName, String seatNumber, Reservation reservation) {
        this();                         // Picks up the isWindow = false in the null constructor.
        this.firstName = firstName;
        this.lastName = lastName;
        this.seatNumber = seatNumber;
        this.reservation = reservation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean getIsWindow() {
        return isWindow;
    }

    public void setIsWindow(boolean window) {
        this.isWindow = isWindow;
    }


}

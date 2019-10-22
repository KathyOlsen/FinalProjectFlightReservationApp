package com.example.demo;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "user_id")
    private User user;
//    matching language in User.java should be:
//        @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Trip> trips;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public Set<Reservation> reservations;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public Set<Passenger> passengers;
//    matching language in Passenger.java should be:
//          @ManyToOne(fetch = FetchType.EAGER)
//          @JoinColumn (name = "passenger_id")
//          private Passenger passenger;

    public Trip() {
    }

    public Trip(User user) {
        this.user = user;
    }

    public Trip(Set<Reservation> reservations, User user, Set<Passenger> passengers) {
        this.reservations = reservations;
        this.user = user;
        this.passengers = passengers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }
}

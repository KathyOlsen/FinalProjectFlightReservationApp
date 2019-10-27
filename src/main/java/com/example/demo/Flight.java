package com.example.demo;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure_airport", nullable = false)
    private String departureAirport;

    @Column(name = "arrival_airport", nullable = false)
    private String arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "basePrice", nullable = false)
    private double basePrice;

    private String departureTimeStr;

    /**
     * The following method will change the flight arrival and departure times from 24
     *  format to 12 hour format.
     *  Note: Changes were made to flightlistadmin.html
     */
    public String twelveHourFormat(LocalTime lt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return lt.format(formatter);
    }

    public Flight() {

    }

    public Flight(String flightNumber, String departureAirport, String arrivalAirport, LocalTime departureTime, int durationMinutes, double basePrice) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.durationMinutes = durationMinutes;
        this.basePrice = basePrice;
    }

    public Flight(String flightNumber, String departureAirport, String arrivalAirport, String departureTimeStr, int durationMinutes, double basePrice) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = LocalTime.parse("00:00:00");       // This is midnight -JK
        this.durationMinutes = durationMinutes;
        this.basePrice = basePrice;

        this.setDepartureTimeStr(departureTimeStr);               // Used to converts an input text field into a LocalTime object
    }

    public Set<String> getAirportList(ArrayList<Flight> flights){
        Set<String> airports = new HashSet<>();
        for(Flight flight : flights){
            airports.add(flight.departureAirport);
            airports.add(flight.arrivalAirport);
        }
        return airports;
    }

    public double getPricePerPassenger(String flightClass, double basePrice){
        double pricePerPassenger;
        if (flightClass.equalsIgnoreCase("economy")){
            pricePerPassenger = basePrice;
        }else if (flightClass.equalsIgnoreCase("business")){
            pricePerPassenger = 2 * basePrice;
        }else{
            pricePerPassenger = 3 * basePrice;
        }
        return pricePerPassenger;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalTime getDepartureTime() { return departureTime; }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public LocalTime calcArrivalTime() {
        return this.departureTime.plusMinutes(durationMinutes);
    }

    public String getDepartureTimeStr() {
        return departureTimeStr;
    }

    public void setDepartureTimeStr(String departureTimeStr) {
        this.departureTimeStr = departureTimeStr;
        this.departureTime = LocalTime.parse(departureTimeStr);
    }
}
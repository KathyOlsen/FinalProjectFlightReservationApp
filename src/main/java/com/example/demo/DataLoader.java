package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

     /** Repositories */
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {
        if(roleRepository.count() <1) {
            // Create roles
            roleRepository.save(new Role("USER"));                  // User role
            roleRepository.save(new Role("ADMIN"));                 // Administrator role

            Role adminRole = roleRepository.findByRole("ADMIN");
            Role userRole = roleRepository.findByRole("USER");

            // Create users	(password, first_name, last_name, enabled, username, birthdate, citizenship, email, phone, role)
            User user1 = new User("Paul", "Waters", "pwaters", "pwdpw", "05/23/1992", "USA", "pwaters@email.com", "301-525-7896");
            user1.setRoles(Arrays.asList(userRole));
            userRepository.save(user1);

            User user2 = new User("Jane", "Garten", "jgarten", "pwdjg", "02/17/2001", "USA", "jgarten@email.com", "301-555-6789");
            user2.setRoles(Arrays.asList(userRole));
            userRepository.save(user2);

            User user3 = new User("Joshua", "Woods", "jwoods", "pwdjw", "09/02/1981", "USA", "jwoods@email.com", "301-555-1234");
            user3.setRoles(Arrays.asList(userRole));
            userRepository.save(user3);

            User user4 = new User("Admin", "User", "auser", "pwdau", "01/21/2000", "USA", "admin@email.com", "111-555-9999");
            user4.setRoles(Arrays.asList(adminRole));
            userRepository.save(user4);

            User user5 = new User("Donna", "Woods", "dwoods", "pwddw", "08/17/1981", "USA", "dwoods@email.com", "301-555-1235");
            User user6 = new User("Ann", "Woods", "awoods", "pwdaw", "03/24/2007", "USA", "awoods@email.com", "301-555-1235");


            // Create flights (flightNumber, departureAirport, arrivalAirport, departureTime, durationMinutes, basePrice)
            //DateFormat sdf = new SimpleDateFormat("hh:mm:ss");

            LocalTime time;
            //String time1 = "9:45:00";
            //time = sdf.parse(time1);
            time = LocalTime.of(9, 45, 00);
            Flight flight1 = new Flight("101", "IAD", "LAX", time, 350, 350.0);
            flightRepository.save(flight1);

            //String time2 = "12:30:00";
            //time = sdf.parse(time2);
            time = LocalTime.of(12, 30, 00);
            Flight flight2 = new Flight("102", "IAD", "LAX", time, 345, 350.0);
            flightRepository.save(flight2);

            //String time3 = "16:50:00";
            //time = sdf.parse(time3);
            time = LocalTime.of(16, 50, 00);
            Flight flight3 = new Flight("103", "IAD", "LAX", time, 355, 350.0);
            flightRepository.save(flight3);

            //String time4 = "9:30:00";
            //time = sdf.parse(time4);
            time = LocalTime.of(9, 30, 00);
            Flight flight4 = new Flight("201", "LAX", "IAD", time, 325, 350.0);
            flightRepository.save(flight4);

            //String time5 = "13:25:00";
            //time = sdf.parse(time5);
            time = LocalTime.of(13, 25, 00);
            Flight flight5 = new Flight("202", "LAX", "IAD", time, 330, 350.0);
            flightRepository.save(flight5);

            //String time6 = "17:40:00";
            //time = sdf.parse(time6);
            time = LocalTime.of(17, 40, 00);
            Flight flight6 = new Flight("203", "LAX", "IAD", time, 320, 350.0);
            flightRepository.save(flight6);

            //String time7 = "8:55:00";
            //time = sdf.parse(time7);
            time = LocalTime.of(8, 55, 00);
            Flight flight7 = new Flight("301", "ORD", "DFW", time, 130, 215.0);
            flightRepository.save(flight7);

            //String time8 = "11:35:00";
            //time = sdf.parse(time8);
            time = LocalTime.of(11, 35, 00);
            Flight flight8 = new Flight("401", "DFW", "ORD", time, 135, 215.0);
            flightRepository.save(flight8);

            // Passenger:
            Passenger pass1 = new Passenger(user1.getFirstName(), user1.getLastName());
            Set set1 = new HashSet();
            set1.add(pass1);
            passengerRepository.save(pass1);

            Passenger pass2 = new Passenger(user2.getFirstName(), user2.getLastName());
            Set set2 = new HashSet();
            set2.add(pass2);
            passengerRepository.save(pass2);

            Passenger pass3 = new Passenger(user3.getFirstName(), user3.getLastName());
            Passenger pass4 = new Passenger(user5.getFirstName(), user5.getLastName());
            Passenger pass5 = new Passenger(user6.getFirstName(), user6.getLastName());
            Set set3 = new HashSet();
            set3.add(pass3);
            set3.add(pass4);
            set3.add(pass5);

            passengerRepository.save(pass3);
            passengerRepository.save(pass4);
            passengerRepository.save(pass5);

            // Reservation: bool isRoundTrip, Date departureDate, Date returnDate, Str flightClass, int numberPassengers, User user, Flgt departure, Flgt arrival, Set passengers
            Date departDate = new Date();
            Date arriveDate = new Date();
            ;
            DateFormat dsdf = new SimpleDateFormat("dd-MM-yyyy");

            String dDate1 = "13-10-2019";
            String aDate1 = "18-10-2019";
            departDate = dsdf.parse(dDate1);
            arriveDate = dsdf.parse(aDate1);
            System.out.println(departDate);
            Reservation rsvr1 = new Reservation(true, departDate, arriveDate, "Economy", 1, user1, flight1, flight4, set1);
            reservationRepository.save(rsvr1);

            String dDate2 = "03-11-2019";
            String aDate2 = "06-11-2019";
            departDate = dsdf.parse(dDate2);
            arriveDate = dsdf.parse(aDate2);
            Reservation rsvr2 = new Reservation(true, departDate, arriveDate, "Business", 1, user2, flight2, flight5, set2);
            reservationRepository.save(rsvr2);

            String dDate3 = "20-12-2019";
            String aDate3 = "29-12-2019";
            departDate = dsdf.parse(dDate3);
            arriveDate = dsdf.parse(aDate3);
            Reservation rsvr3 = new Reservation(true, departDate, arriveDate, "Economy", 3, user2, flight3, flight6, set3);
            reservationRepository.save(rsvr3);
        }
    }
}



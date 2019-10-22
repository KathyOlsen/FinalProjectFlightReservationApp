package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception {
        // Create roles
        roleRepository.save(new Role("USER"));                  // User role
        roleRepository.save(new Role("ADMIN"));                 // Administrator role

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        // Create users	(password, first_name, last_name, enabled, username, birthdate, citizenship, email, phone, role)
        User user;
        user = new User("pwdpw", "Paul", "Waters", true, "pwaters", "05/23/1992", "USA", "pwaters@email.com", "301-525-7896");
        user.setRoles(Arrays.asList(userRole));

        userRepository.save(user);

        user = new User("pwdjg", "Jane", "Garten", true, "jgarten", "02/17/2001", "USA", "jgarten@email.com", "301-555-6789");
        user.setRoles(Arrays.asList(userRole));

        userRepository.save(user);

        user = new User("pwdjw", "Joshua", "Woods", true, "jwoods", "09/02/1981", "USA", "jwoods@email.com", "301-555-1234");
        user.setRoles(Arrays.asList(userRole));

        userRepository.save(user);

        user = new User("pwdau", "Admin", "User", true, "auser", "01/21/2000", "USA", "admin@email.com", "111-555-9999");
        user.setRoles(Arrays.asList(adminRole));

        userRepository.save(user);


        // Create flights (flightNumber, departureAirport, arrivalAirport, departureTime, durationMinutes, basePrice)
        Flight flight;
        Date date;

        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String time1 = "9:45:00";
        date = new Date();
        date = sdf.parse(time1);
        flight = new Flight("101", "IAD", "LAX", date, 350, 350.0);

        String time2 = "12:30:00";
        date = new Date();
        date = sdf.parse(time2);
        flight = new Flight("102", "IAD", "LAX", date, 350, 350.0);

        String time3 = "16:50:00";
        date = new Date();
        date = sdf.parse(time3);
        flight = new Flight("103", "IAD", "LAX", date, 350, 350.0);

        String time4 = "9:30:00";
        date = new Date();
        date = sdf.parse(time4);
        flight = new Flight("104", "IAD", "LAX", date, 350, 350.0);

        String time5 = "13:25:00";
        date = new Date();
        date = sdf.parse(time5);
        flight = new Flight("105", "LAX", "LAX", date, 350, 350.0);

        String time6 = "15:30:00";
        date = new Date();
        date = sdf.parse(time6);
        flight = new Flight("106", "IAD", "LAX", date, 350, 350.0);

        String time7 = "15:30:00";
        date = new Date();
        date = sdf.parse(time7);
        flight = new Flight("107", "IAD", "LAX", date, 350, 350.0);

        String time8 = "15:30:00";
        date = new Date();
        date = sdf.parse(time8);
        flight = new Flight("108", "IAD", "LAX", date, 350, 350.0);






    }
}



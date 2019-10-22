package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
/*
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
*/

    }
}



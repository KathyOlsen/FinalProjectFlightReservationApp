package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Method: User findByEmail
     * @param email
     * @return
     */
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Method: Long countByEmail
     * @param email
     * @return
     */
    public Long countByEmail(String email){
        return userRepository.countByEmail(email);
    }

    /**
     * Method: User findByUsername
     * @param username
     * @return
     */
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /**
     * Method: void saveUser
     * @param user
     */
    public void saveUser(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Method: void saveAdmin
     * @param user
     */
    public void saveAdmin(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Method: User getUser
     * @return
     */
    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }
}

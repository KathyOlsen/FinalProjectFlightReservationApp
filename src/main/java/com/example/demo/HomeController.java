package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    ReservationRepository reservationRepository;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/flighthistory")
    public String showFlightHistory(Model model){
        User user = userService.getUser();
        Date today = new Date();
        model.addAttribute("myreservations", reservationRepository
                .findByUserAndDepartureDateIsBefore(user,today));
        return "/flighthistory";
    }

}

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class SecurityController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        if(result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user);
            model.addAttribute("message","User Account Created");
        }
        return "login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(){
        return "secure";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/addFlight")
    public String addFlight(Model model) {
        model.addAttribute("flight", new Flight());
        return "flightform";
    }

    @PostMapping("/processflight")
    public String processFlight(@Valid Flight flight,
    BindingResult result){
        if(result.hasErrors()){
            return "flightform";
        }

        flightRepository.save(flight);
        return "redirect:/admin";
    }

    @RequestMapping("/rolelist")
    public String roleList(Model model){
        model.addAttribute("roles", roleRepository.findAll());
        return "rolelist";
    }

    @RequestMapping("/userlist")
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "userlist";
    }

    @RequestMapping("/detail_role/{id}")
    public String showRole(@PathVariable("id") long id, Model model){
        model.addAttribute("role", roleRepository.findById(id).get());
        return "showrole";
    }

    @RequestMapping("/detail_user/{id}")
    public String showUser(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userRepository.findById(id).get());
        model.addAttribute("roles", roleRepository.findAll());
        return "showuser";
    }

}

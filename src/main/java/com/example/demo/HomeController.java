package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String index(){
        return "index";
    }


    /*
    @GetMapping("/addUser")                                             // For Admin role only
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "userForm";
    }

    @RequestMapping("/rolelist")                                        // For Admin role only
    public String roleList(Model model){
        model.addAttribute("roles", roleRepository.findAll());
        return "showrole";
    }

    @RequestMapping("/userlist")                                        // For Admin role only
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "showuser";
    }
    */
}

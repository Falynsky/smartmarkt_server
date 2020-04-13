package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.services.AppUsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {

    private AppUsersService appUsersService;

    public HelloController(AppUsersService appUsersService) {
        this.appUsersService = appUsersService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello-user")
    public String helloUser() {
        return "hello-user";
    }

    @GetMapping("/hello-admin")
    public String helloAdmin() {
        return "hello-admin";
    }

    @GetMapping("/sing-up")
    public String singUp(Model model) {
        model.addAttribute("user", new AppUsers());
        return "sing-up";
    }

    @PostMapping("/register")
    public String register(AppUsers appUsers) {
        appUsersService.createAndAddUser(appUsers);
        return "sing-up";
    }

}

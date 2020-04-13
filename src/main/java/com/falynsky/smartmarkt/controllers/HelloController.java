package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.services.AppUsersService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@Controller
public class HelloController {

    private AppUsersService appUsersService;

    public HelloController(AppUsersService appUsersService) {
        this.appUsersService = appUsersService;
    }

    @GetMapping("/hello")
    public String helloAdmin(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        model.addAttribute("authorities", authorities);
        model.addAttribute("details", details);
        return "hello";
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

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.services.AccountService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Collection;

@Controller
public class SingUpController {

    private final AccountService accountService;

    public SingUpController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/helloOld")
    public String helloAdmin(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        model.addAttribute("authorities", authorities);
        model.addAttribute("details", details);
        return "hello";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("user", new User());
        model.addAttribute("licence", new Licence());
        return "sign-up";
    }

    @PostMapping("/register")
    public String register(Account newAccount, Licence newLicence, User newUser) {
        accountService.createNewAccountData(newAccount, newLicence, newUser);
        return "sign-up";
    }
}

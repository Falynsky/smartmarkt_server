package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SingUpController {

    private final AccountService accountService;

    public SingUpController(AccountService accountService) {
        this.accountService = accountService;
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

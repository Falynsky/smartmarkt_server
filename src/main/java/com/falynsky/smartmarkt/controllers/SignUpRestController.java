package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/signUp")
public class SignUpRestController {

    private final AccountService accountService;

    public SignUpRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, Object> map) {

        try {
            Account newAccount = new Account();
            newAccount.setUsername((String) map.get("username"));
            newAccount.setPassword((String) map.get("password"));

            Licence newLicence = new Licence();
            newLicence.setLicenceKey((String) map.get("licenceKey"));

            User newUser = new User();
            newUser.setFirstName((String) map.get("firstName"));
            newUser.setLastName((String) map.get("lastName"));
            accountService.createNewAccountData(newAccount, newLicence, newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendCorrectResponse();
    }


    private ResponseEntity<Map<String, Object>> sendCorrectResponse() {
        Map<String, Object> body = new HashMap<>();
        return ResponseEntity.ok(body);
    }


}

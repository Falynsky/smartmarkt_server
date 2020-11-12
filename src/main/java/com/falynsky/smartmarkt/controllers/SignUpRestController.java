package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.services.AccountService;
import com.falynsky.smartmarkt.services.ResponseMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> map) {
        try {
            Account newAccount = createAccount(map);
            Licence newLicence = createLicence(map);
            User newUser = createUser(map);
            accountService.createNewAccountData(newAccount, newLicence, newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMsgService.sendCorrectResponse();
    }

    private Account createAccount(Map<String, Object> map) {
        Account newAccount = new Account();
        newAccount.setUsername((String) map.get("username"));
        newAccount.setPassword((String) map.get("password"));
        return newAccount;
    }

    private Licence createLicence(Map<String, Object> map) {
        Licence newLicence = new Licence();
        newLicence.setLicenceKey((String) map.get("licenceKey"));
        return newLicence;
    }

    private User createUser(Map<String, Object> map) {
        User newUser = new User();
        newUser.setFirstName((String) map.get("firstName"));
        newUser.setLastName((String) map.get("lastName"));
        return newUser;
    }

}

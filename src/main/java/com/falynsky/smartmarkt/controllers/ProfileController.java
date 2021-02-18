package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.objects.Account;
import com.falynsky.smartmarkt.models.objects.User;
import com.falynsky.smartmarkt.services.AccountService;
import com.falynsky.smartmarkt.services.UserService;
import com.falynsky.smartmarkt.utils.ResponseMapUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final AccountService accountService;
    private final UserService userService;

    public ProfileController(
            AccountService accountService,
            UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/getInfo")
    public Map<String, Object> getProfileInfo(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        Map<String, Object> data = new HashMap<>();
        try {
            User currentUser = getCurrentUser(userToken);
            data.put("firstName", currentUser.getFirstName());
            data.put("lastName", currentUser.getLastName());
            data.put("userId", currentUser.getId());
        } catch (Exception ex) {
            return ResponseMapUtils.buildResponse(data, false);
        }
        return ResponseMapUtils.buildResponse(data, true);
    }

    private User getCurrentUser(String userToken) throws Exception {
        Account account = accountService.getCurrentAccount(userToken);
        return userService.getCurrentUserByAccount(account);
    }

}

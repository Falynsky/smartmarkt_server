package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.JWT.utils.JwtTokenUtil;
import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.repositories.AccountRepository;
import com.falynsky.smartmarkt.repositories.UserRepository;
import com.falynsky.smartmarkt.utils.ResponseMapBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ProfileController(JwtTokenUtil jwtTokenUtil,
                             AccountRepository accountRepository,
                             UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
            return ResponseMapBuilder.buildResponse(data, false);
        }
        return ResponseMapBuilder.buildResponse(data, true);
    }

    private User getCurrentUser(String userToken) throws Exception {
        Account account = getCurrentAccount(userToken);
        return getCurrentUserByAccount(account);
    }

    private User getCurrentUserByAccount(Account account) throws Exception {
        Optional<User> optionalUser = userRepository.findFirstByAccountId(account);
        if (!optionalUser.isPresent()) {
            throw new Exception("USER NOT FOUND");
        }
        return optionalUser.get();
    }

    private Account getCurrentAccount(String userToken) throws Exception {
        String currentUserUsername = jwtTokenUtil.getUsernameFromToken(userToken.substring(5));

        Optional<Account> optionalAccount = accountRepository.findByUsername(currentUserUsername);
        if (!optionalAccount.isPresent()) {
            throw new Exception("ACCOUNT NOT FOUND");
        }
        return optionalAccount.get();
    }
}

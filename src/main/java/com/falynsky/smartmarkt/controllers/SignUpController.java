package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.objects.User;
import com.falynsky.smartmarkt.models.request.RegisterObject;
import com.falynsky.smartmarkt.services.AccountService;
import com.falynsky.smartmarkt.utils.ResponseUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/signUp")
public class SignUpController {

    private final AccountService accountService;

    public SignUpController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterObject registerObject) {
        try {

            boolean isInvalidRegisterInput = isInvalidRegisterInput(registerObject);
            if (isInvalidRegisterInput) {
                return ResponseUtils.errorResponse("Wartości są wymagane i puste znaki są zabronione.");
            }
            User newUser = createUser(registerObject);
            accountService.createNewAccountData(registerObject, newUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseUtils.errorResponse("Podany użytkownik już istnieje.");
        } catch (Exception e) {
            return ResponseUtils.errorResponse("Błąd serwera!", "Spróbuj ponownie później.");
        }
        return ResponseUtils.sendCorrectResponse();
    }

    private boolean isInvalidRegisterInput(RegisterObject registerObject) {

        String username = registerObject.username;
        String password = registerObject.password;
        String firstName = registerObject.firstName;
        String lastName = registerObject.lastName;

        if (username == null || password == null || firstName == null || lastName == null) {
            return true;
        }

        boolean isUsernameValid = isDataValid(username);
        boolean isPasswordValid = isDataValid(password);
        boolean isFirstNameValid = isDataValid(firstName);
        boolean isLastNameValid = isDataValid(lastName);

        return isUsernameValid || isPasswordValid || isFirstNameValid || isLastNameValid;
    }

    private boolean isDataValid(String username) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher usernameMatcher = pattern.matcher(username);
        return usernameMatcher.find();
    }

    private User createUser(RegisterObject registerObject) {
        String firstName = registerObject.firstName;
        String lastName = registerObject.lastName;
        return new User(firstName, lastName);
    }

}

package com.falynsky.smartmarkt.JWT.controllers;

import com.falynsky.smartmarkt.JWT.utils.JwtTokenUtil;
import com.falynsky.smartmarkt.JWT.services.JwtUserDetailsService;
import com.falynsky.smartmarkt.JWT.models.JwtRequest;
import com.falynsky.smartmarkt.JWT.models.JwtResponse;
import com.falynsky.smartmarkt.services.ResponseMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        authenticate(username, password);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            return ResponseMsgService.errorResponse("Brak danych użytkownika!");
        }

        final String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> response = new HashMap<>();
        JwtResponse jwtResponse = new JwtResponse(token);
        response.put("data", jwtResponse);
        return ResponseEntity.ok(response);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
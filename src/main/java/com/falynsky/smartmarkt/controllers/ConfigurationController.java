package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.services.ResponseMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class ConfigurationController {

    @PostMapping("/isAlive")
    public ResponseEntity<Map<String, Object>> isAlive() {
        return ResponseMsgService.sendCorrectResponse();
    }

}

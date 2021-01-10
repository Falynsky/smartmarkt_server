package com.falynsky.smartmarkt.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseMsgService {

    public static ResponseEntity<Map<String, Object>> sendCorrectResponse() {
        Map<String, Object> body = new HashMap<>();
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<Map<String, Object>> sendCorrectResponse(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", message);
        return ResponseEntity.ok(body);
    }


    public static ResponseEntity<Map<String, Object>> errorResponse(String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", "false");
        body.put("msg", msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    public static ResponseEntity<Map<String, Object>> errorResponse(String title, String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", "false");
        body.put("title", title);
        body.put("msg", msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    public static ResponseEntity<Map<String, Object>> elementNotFoundResponse() {
        Map<String, Object> body = new HashMap<>();
        body.put("success", "false");
        body.put("msg", "ObjectNotFound");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}

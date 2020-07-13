package com.falynsky.smartmarkt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    Map<String, Object> body = new LinkedHashMap<>();

    @ExceptionHandler(NotEnoughQuantity.class)
    public ResponseEntity<Object> handleNotEnoughQuantityException(NotEnoughQuantity e) {

        body.put("success", "false");
        body.put("msg", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

package com.falynsky.smartmarkt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Not enough quantity of product")
public class NotEnoughQuantity extends Exception {

    private static final String EXCEPTION_MESSAGE = "Not enough quantity of product";

    public NotEnoughQuantity() {
        super(EXCEPTION_MESSAGE);
    }
}

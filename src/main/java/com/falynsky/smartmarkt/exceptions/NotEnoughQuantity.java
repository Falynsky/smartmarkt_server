package com.falynsky.smartmarkt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Niewystarczająca liczba produktów")
public class NotEnoughQuantity extends Exception {

    private static final String EXCEPTION_MESSAGE = "Niewystarczająca liczba produktów";

    public NotEnoughQuantity() {
        super(EXCEPTION_MESSAGE);
    }

    public NotEnoughQuantity(String name) {
        super(EXCEPTION_MESSAGE + " - " + name);
    }

}

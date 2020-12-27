package com.falynsky.smartmarkt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Nie wystarczająca liczba produktów")
public class NotEnoughQuantity extends Exception {

    private static final String EXCEPTION_MESSAGE = "Nie wystarczająca liczba produktów";
    private static final String EXCEPTION_MESSAGE_WITH_RPODUCT_NAME = "Nie wystarczająca liczba produktów - ";

    public NotEnoughQuantity() {
        super(EXCEPTION_MESSAGE);
    }

    public NotEnoughQuantity(String name) {
        super((EXCEPTION_MESSAGE_WITH_RPODUCT_NAME+name).toString());
    }

}

package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Car not for sale")
public class CarNotForSaleException extends RuntimeException {

    public CarNotForSaleException() {
    }

    public CarNotForSaleException(String message) {
        super(message);
    }
}

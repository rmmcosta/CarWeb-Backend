package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Car with the same plate found")
public class CarWithSamePlateException extends RuntimeException {

    public CarWithSamePlateException() {
    }

    public CarWithSamePlateException(String message) {
        super(message);
    }
}

package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Order not found")
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}

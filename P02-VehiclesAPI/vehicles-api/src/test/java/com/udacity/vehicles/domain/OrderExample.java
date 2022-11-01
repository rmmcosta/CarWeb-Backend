package com.udacity.vehicles.domain;

import com.udacity.vehicles.domain.order.Order;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderExample {
    public static Order getOrder() {
        Order order = new Order();
        order.setBuyer("Ricardo Costa");
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryLocation(new Location(0d, 0d));
        order.setCarSet(Set.of(CarExample.getCar()));
        return order;
    }
}

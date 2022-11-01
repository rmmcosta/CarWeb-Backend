package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OrderResourcesEntity {
    OrderList _embedded;
    SelfEntity _links;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class OrderList {
        List<Order> orderList;
    }
}

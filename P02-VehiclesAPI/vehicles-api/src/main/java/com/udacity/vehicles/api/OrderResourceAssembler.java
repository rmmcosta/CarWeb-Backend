package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.order.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Maps the OrderController to the Order class using HATEOAS
 */
@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {

    @Override
    public Resource<Order> toResource(Order order) {
        return new Resource<>(order,
                linkTo(methodOn(OrderController.class).get(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).list()).withRel("orders"));

    }
}

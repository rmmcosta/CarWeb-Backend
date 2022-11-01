package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.order.Order;
import com.udacity.vehicles.service.OrderService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Implements a REST-based controller for the Orders API.
 */
@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderService orderService;
    private final OrderResourceAssembler assembler;

    OrderController(OrderService orderService, OrderResourceAssembler assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any orders.
     *
     * @return list of orders
     */
    @GetMapping
    Resources<Resource<Order>> list() {
        List<Resource<Order>> resources = orderService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(OrderController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific order by ID.
     *
     * @param id the id number of the given order
     * @return all information for the requested order
     */
    @GetMapping("/{id}")
    Resource<Order> get(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return assembler.toResource(order);
    }

    /**
     * Posts information to create a new order in the system.
     *
     * @param order A new order to add to the system.
     * @return response that the new order was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Order order) throws URISyntaxException {
        Order savedOrder = orderService.save(order);
        if (savedOrder.getId() == null) {
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        }
        Resource<Order> resource = assembler.toResource(savedOrder);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Updates the information of an order in the system.
     *
     * @param id    The ID number for which to update order information.
     * @param order The updated information about the related order.
     * @return response that the order was updated in the system
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Order order) {
        order.setId(id);
        Resource<Order> resource = assembler.toResource(orderService.save(order));
        return ResponseEntity.ok(resource);
    }

    /**
     * Removes an order from the system.
     *
     * @param id The ID number of the order to remove.
     * @return response that the related order is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

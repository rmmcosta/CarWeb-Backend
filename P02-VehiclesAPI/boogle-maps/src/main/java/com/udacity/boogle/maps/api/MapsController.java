package com.udacity.boogle.maps.api;

import com.udacity.boogle.maps.repository.Address;
import com.udacity.boogle.maps.repository.MockAddressRepository;
import com.udacity.boogle.maps.service.MockAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maps")
public class MapsController {
    @Autowired
    MockAddressService mockAddressService;

    @GetMapping
    public Address get(@RequestParam Double lat, @RequestParam Double lon) {
        return mockAddressService.getAddress(lat,lon);
    }
}

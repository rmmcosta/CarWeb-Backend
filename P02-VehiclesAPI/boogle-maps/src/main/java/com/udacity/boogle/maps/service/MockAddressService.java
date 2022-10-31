package com.udacity.boogle.maps.service;

import com.udacity.boogle.maps.repository.Address;
import com.udacity.boogle.maps.repository.MockAddressRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MockAddressService {
    Map<String, Address> addressesInMemory;

    public MockAddressService() {
        addressesInMemory = new HashMap<>();
    }

    public Address getAddress(Double lat, Double lon) {
        String latAndLon = lat + "," + lon;
        if (addressesInMemory.containsKey(latAndLon)) {
            return addressesInMemory.get(latAndLon);
        } else {
            Address address = MockAddressRepository.getRandom();
            addressesInMemory.put(latAndLon, address);
            return address;
        }
    }
}

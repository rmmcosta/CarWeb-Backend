package com.udacity.vehicles.domain.manufacturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {
    @Autowired
    ManufacturerRepository manufacturerRepository;

    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public Manufacturer findById(Integer id) {
        return manufacturerRepository.findById(id).orElseThrow(ManufacturerNotFoundException::new);
    }
}

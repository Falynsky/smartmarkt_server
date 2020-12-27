package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.SalesDTO;
import com.falynsky.smartmarkt.repositories.SalesRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesRepository salesRepository;

    public SalesController(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @GetMapping("/all")
    public List<SalesDTO> getAllSales() {
        return salesRepository.retrieveSalesAsDTO();
    }
}

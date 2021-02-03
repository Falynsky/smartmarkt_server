package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.SalesDTO;
import com.falynsky.smartmarkt.repositories.SalesRepository;
import com.falynsky.smartmarkt.utils.ResponseMapUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesRepository salesRepository;

    public SalesController(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @GetMapping("/all")
    public Map<String, Object> getAllSales() {
        List<SalesDTO> salesDTOS = salesRepository.retrieveSalesAsDTO();
        return ResponseMapUtils.buildResponse(salesDTOS, true);
    }
}

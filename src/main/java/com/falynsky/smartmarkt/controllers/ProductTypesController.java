package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.repositories.ProductTypeRepository;
import com.falynsky.smartmarkt.utils.ResponseMapBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/productType")
public class ProductTypesController {

    final ProductTypeRepository productTypeRepository;

    public ProductTypesController(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @GetMapping("/all")
    public Map<String, Object> getAllProducts() {

        List<ProductTypeDTO> productTypeDTOS = productTypeRepository.retrieveProductTypesAsDTO();
        return ResponseMapBuilder.buildResponse(productTypeDTOS, true);
    }
}

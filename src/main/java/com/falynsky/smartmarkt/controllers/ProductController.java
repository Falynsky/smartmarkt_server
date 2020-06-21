package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/id")
    public ProductDTO gatProductById(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        return productRepository.retrieveProductAsDTObyId(id);
    }

    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productRepository.retrieveProductAsDTO();
    }

    @PostMapping("/typeId")
    public List<ProductDTO> getAllProductsByTypeId(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        return productRepository.retrieveProductAsDTObyTypeId(id);
    }
}

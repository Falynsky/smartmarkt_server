package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    final ProductRepository productRepository;
    final JdbcTemplate jdbcTemplate;


    public ProductController(ProductRepository productRepository, JdbcTemplate jdbcTemplate) {
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
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
    public ResponseEntity<List<ProductDTO>> getAllProductsByTypeId(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        List<ProductDTO> productsList = productRepository.retrieveProductAsDTObyTypeId(id);
        return ResponseEntity.ok(productsList);
    }
}

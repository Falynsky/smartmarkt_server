package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.dto.ProductDTO;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import com.falynsky.smartmarkt.utils.ResponseMapUtils;
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

    @PostMapping("/typeId")
    public ResponseEntity<Map<String, Object>> getAllProductsByTypeId(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        List<ProductDTO> productsList = productRepository.retrieveProductAsDTObyTypeId(id);
        Map<String, Object> body = ResponseMapUtils.buildResponse(productsList);
        return ResponseEntity.ok(body);
    }


}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import com.falynsky.smartmarkt.utils.ResponseMapBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

//    @PostMapping("/id")
//    public ProductDTO gatProductById(@RequestBody Map<String, Object> map) {
//        Integer id = (Integer) map.get("id");
//        Optional<ProductDTO> productDTO = productRepository.retrieveProductAsDTObyId(id);
//        return productDTO.orElse(null);
//    }
//
//    @GetMapping("/all")
//    public List<ProductDTO> getAllProducts() {
//        return productRepository.retrieveProductAsDTO();
//    }

    @PostMapping("/typeId")
    public ResponseEntity<Map<String, Object>> getAllProductsByTypeId(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        List<ProductDTO> productsList = productRepository.retrieveProductAsDTObyTypeId(id);
        ResponseMapBuilder<List<ProductDTO>> responseMapBuilder = new ResponseMapBuilder<>();
        Map<String, Object> body = responseMapBuilder.buildResponse(productsList);
        return ResponseEntity.ok(body);
    }


}

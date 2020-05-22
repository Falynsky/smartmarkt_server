package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ProductDTO gatProductById(@PathVariable("id") Integer id) {
        return productRepository.retrieveProductAsDTObyId(id);
    }

    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productRepository.retrieveProductAsDTO();
    }
}

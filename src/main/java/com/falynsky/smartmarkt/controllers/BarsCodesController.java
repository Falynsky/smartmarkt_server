package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/barsCodes")
public class BarsCodesController {

    private final ProductService productService;

    public BarsCodesController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{code}")
    public Map<String, Object> getAllUsers(@PathVariable("code") Integer barsCodeCode) {
        final ProductDTO productDTO = productService.getProductDTOByBarsCode(barsCodeCode);
        if(productDTO == null) {
            return new HashMap<>();
        }
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", productDTO.name);
        productData.put("price", productDTO.price);
        productData.put("currency", productDTO.currency);
        return productData;
    }
}

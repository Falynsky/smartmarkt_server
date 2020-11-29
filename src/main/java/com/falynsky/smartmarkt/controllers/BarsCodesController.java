package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.services.ProductService;
import com.falynsky.smartmarkt.services.ProductTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/barsCodes")
public class BarsCodesController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;

    public BarsCodesController(ProductService productService, ProductTypeService productTypeService) {
        this.productService = productService;
        this.productTypeService = productTypeService;
    }

    @GetMapping("/{code}")
    public Map<String, Object> getAllUsers(@PathVariable("code") Integer barsCodeCode) {
        final ProductDTO productDTO = productService.getProductDTOByBarsCode(barsCodeCode);
        if (productDTO == null) {
            return new HashMap<>();
        }

        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productDTO.id);
        productData.put("name", productDTO.name);
        productData.put("price", productDTO.price);
        productData.put("currency", productDTO.currency);
        productData.put("quantity", productDTO.quantity);

        int productTypeId = productDTO.productTypeId;
        final ProductTypeDTO productTypeDTO = productTypeService.getProductTypeDTOById(productTypeId);

        productData.put("productTypeId", productTypeDTO.id);
        productData.put("productType", productTypeDTO.name);

        return productData;
    }
}

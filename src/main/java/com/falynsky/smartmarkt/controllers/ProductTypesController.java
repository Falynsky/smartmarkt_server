package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.repositories.ProductTypeRepository;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/id")
    public ProductTypeDTO gatProductById(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        return productTypeRepository.retrieveProductTypeAsDTObyId(id);
    }

    @GetMapping("/name")
    public ProductTypeDTO gatProductByName(@RequestBody Map<String, Object> map) {
        String name = (String) map.get("name");
        return productTypeRepository.retrieveProductAsDTObyName(name);
    }

    @GetMapping("/get")
    public List<ProductTypeDTO> getProducts(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
        List<String> names = (List<String>) map.get("names");
        return productTypeRepository.retrieveProductTypesAllAsDTO(ids, names);
    }

    @GetMapping("/all")
    public List<ProductTypeDTO> getAllProducts() {
        return productTypeRepository.retrieveProductTypesAsDTO();
    }
}

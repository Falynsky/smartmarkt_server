package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.ProductTypeDTO;
import com.falynsky.smartmarkt.repositories.ProductTypeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/productType")
public class ProductTypesController {

    final ProductTypeRepository productTypeRepository;

    public ProductTypesController(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }
//
//    @GetMapping("/id")
//    public ProductTypeDTO gatProductById(@RequestBody Map<String, Object> map) {
//        Integer id = (Integer) map.get("id");
//        Optional<ProductTypeDTO> productTypeDTO = productTypeRepository.retrieveProductTypeAsDTObyId(id);
//        return productTypeDTO.orElse(null);
//    }
//
//    @GetMapping("/name")
//    public HashMap<String, Object> gatProductByName(@RequestBody Map<String, Object> map) {
//        String name = (String) map.get("name");
//        ProductTypeDTO productTypeDTO = productTypeRepository.retrieveProductAsDTObyName(name);
//        return new HashMap<String, Object>() {
//            {
//                put("success", true);
//                put("data", productTypeDTO);
//            }
//        };
//    }
//
//    @SuppressWarnings("unchecked")
//    @GetMapping("/get")
//    public HashMap<String, Object> getProducts(@RequestBody Map<String, Object> map) {
//        List<Integer> ids = (List<Integer>) map.get("ids");
//        List<String> names = (List<String>) map.get("names");
//        List<ProductTypeDTO> productTypeDTOS = productTypeRepository.retrieveProductTypesAllAsDTO(ids, names);
//        return new HashMap<String, Object>() {
//            {
//                put("success", true);
//                put("data", productTypeDTOS);
//            }
//        };
//    }

    @GetMapping("/all")
    public HashMap<String, Object> getAllProducts() {

        List<ProductTypeDTO> productTypeDTOS = productTypeRepository.retrieveProductTypesAsDTO();

        return new HashMap<String, Object>() {
            {
                put("data", productTypeDTOS);
                put("success", true);
            }
        };
    }
}

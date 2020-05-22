package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/baskets_products")
public class BasketProductController {

    BasketProductRepository basketProductRepository;

    public BasketProductController(BasketProductRepository basketProductRepository) {
        this.basketProductRepository = basketProductRepository;
    }

    @GetMapping("/basket/{id}")
    public BasketProductDTO getBasketsByBasketId(@PathVariable("id") Integer id) {
        return basketProductRepository.retrieveBasketProductAsDTObyBasketId(id);
    }

    @GetMapping("/product/{id}")
    public BasketProductDTO getBasketsByProductId(@PathVariable("id") Integer id) {
        return basketProductRepository.retrieveBasketProductAsDTObyProductId(id);
    }

    @GetMapping("/all")
    public List<BasketProductDTO> getAllBaskets() {
        return basketProductRepository.retrieveBasketProductAsDTO();
    }
}

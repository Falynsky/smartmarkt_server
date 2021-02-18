package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.dto.BasketDTO;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/baskets")
public class BasketController {

    private final BasketRepository basketRepository;

    public BasketController(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @GetMapping("/all")
    public List<BasketDTO> getAllBaskets() {
        return basketRepository.retrieveBasketsAsDTO();
    }

    @GetMapping("/{id}")
    public BasketDTO getAllUsers(@PathVariable("id") Integer id) {
        return basketRepository.retrieveBasketAsDTObyId(id);
    }
}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.BasketProduct;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import com.falynsky.smartmarkt.services.BasketProductService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@Log4j2
@RequestMapping("/baskets_products")
public class BasketProductController {

    BasketProductRepository basketProductRepository;
    BasketRepository basketRepository;
    ProductRepository productRepository;
    AccountRepository accountRepository;
    UserRepository userRepository;
    BasketProductService basketProductService;

    public BasketProductController(BasketProductRepository basketProductRepository,
                                   BasketRepository basketRepository,
                                   ProductRepository productRepository,
                                   BasketProductService basketProductService,
                                   AccountRepository accountRepository,
                                   UserRepository userRepository) {
        this.basketProductRepository = basketProductRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.basketProductService = basketProductService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getUserProducts")
    public List<BasketProductDTO> getBasketProductsByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        Basket basket = basketProductService.getUserBasket(userToken);
        return basketProductRepository.retrieveBasketProductAsDTObyBasketId(basket.getId());
    }

    @GetMapping("/product/{id}")
    public BasketProductDTO getBasketsByProductId(@PathVariable("id") Integer id) {
        return basketProductRepository.retrieveBasketProductAsDTObyProductId(id);
    }

    @GetMapping("/all")
    public List<BasketProductDTO> getAllBaskets() {
        return basketProductRepository.retrieveBasketProductAsDTO();
    }

    @GetMapping("/log")
    public void log() {
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        BasketProduct basketProduct = basketProductService.getOrCreateBasketProduct(map, userToken);
        Map<String, Object> body = new HashMap<>();
        try {
            basketProductRepository.save(basketProduct);
        } catch (Exception ex) {
            return sendErrorResponse(body, ex);
        }
        return setCorrectResponse(body);
    }

    private ResponseEntity<Map<String, Object>> sendErrorResponse(Map<String, Object> body, Exception ex) {
        body.put("success", "false");
        body.put("msg", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> setCorrectResponse(Map<String, Object> body) {
        body.put("success", "true");
        return ResponseEntity.ok(body);
    }
}

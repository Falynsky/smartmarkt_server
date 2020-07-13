package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.BasketProduct;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import com.falynsky.smartmarkt.services.BasketProductService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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
    public Map<String, Object> getBasketProductsByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        List<Map<String, Object>> basketProductsData = basketProductService.getSelectedBasketProductsData(basket);

        return Map.of(
                "success", true,
                "data", basketProductsData
        );
    }

    @GetMapping("/product/{id}")
    public BasketProductDTO getBasketsByProductId(@PathVariable("id") Integer id) {
        return basketProductRepository.retrieveBasketProductAsDTObyProductId(id);
    }

    @GetMapping("/all")
    public List<BasketProductDTO> getAllBaskets() {
        return basketProductRepository.retrieveBasketProductAsDTO();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {

        basketProductService.updateOrCreateBasketProduct(map, userToken);
        return sendCorrectResponse();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addOne")
    public ResponseEntity<Map<String, Object>> addOneToBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {


        try {
            basketProductService.addOneToBasketProduct(map, userToken);

        } catch (Exception ex) {
            return sendErrorResponse(ex.getMessage());
        }
        return sendCorrectResponse();
    }


    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        try {
            basketProductService.removeProductFromBasket(map, userToken);
        } catch (Exception ex) {
            return sendErrorResponse(ex.getMessage());
        }
        return sendCorrectResponse();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/removeOne")
    public ResponseEntity<Map<String, Object>> removeOneFromBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        BasketProduct basketProduct = basketProductService.getBasketProduct(map, userToken);
        if (basketProduct == null) {
            return elementNotFoundResponse();
        }
        try {
            if (basketProduct.getQuantity() == 1) {
                basketProductRepository.delete(basketProduct);
            } else if (basketProduct.getQuantity() > 1) {
                int newQuantity = basketProduct.getQuantity() - 1;
                basketProduct.setQuantity(newQuantity);
                basketProductRepository.save(basketProduct);
            }
        } catch (Exception ex) {
            return sendErrorResponse(ex.getMessage());
        }
        return sendCorrectResponse();
    }

    private ResponseEntity<Map<String, Object>> sendErrorResponse(String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", "false");
        body.put("msg", msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ResponseEntity<Map<String, Object>> sendCorrectResponse() {
        Map<String, Object> body = new HashMap<>();
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Map<String, Object>> elementNotFoundResponse() {
        Map<String, Object> body = new HashMap<>();
        body.put("success", "false");
        body.put("msg", "ObjectNotFound");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}

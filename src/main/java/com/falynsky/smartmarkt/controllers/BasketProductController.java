package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.JWT.JwtTokenUtil;
import com.falynsky.smartmarkt.models.*;
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
import java.util.Optional;

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
    private final JwtTokenUtil jwtTokenUtil;

    public BasketProductController(BasketProductRepository basketProductRepository,
                                   BasketRepository basketRepository,
                                   ProductRepository productRepository,
                                   BasketProductService basketProductService,
                                   JwtTokenUtil jwtTokenUtil,
                                   AccountRepository accountRepository,
                                   UserRepository userRepository) {
        this.basketProductRepository = basketProductRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.basketProductService = basketProductService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/log")
    public void log() {
//        log.trace("A TRACE Message");
//        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToBasket(@RequestBody Map<String, Object> map, @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        Integer productId = (Integer) map.get("productId");
        Object quantityValue = map.get("quantity");
        Float quantity = null;
        if (quantityValue instanceof Double) {
            quantity = ((Double) quantityValue).floatValue();
        } else if (quantityValue instanceof Integer) {
            quantity = ((Integer) quantityValue).floatValue();
        }

        if (quantity == null) {
            throw new Exception("NO QUANTITY");
        }

        String currentUserUsername = jwtTokenUtil.getUsernameFromToken(userToken.substring(5));
        Optional<Account> optionalAccount = accountRepository.findByUsername(currentUserUsername);
        if (optionalAccount.isEmpty()) {
            throw new Exception("ACCOUNT NOT FOUND");
        }
        Account account = optionalAccount.get();
        Optional<User> optionalUser = userRepository.findFirstByAccountId(account);
        if (optionalUser.isEmpty()) {
            throw new Exception("USER NOT FOUND");
        }
        User user = optionalUser.get();
        Optional<Basket> optionalBasket = basketRepository.findByUserId(user);
        Basket basket = null;
        if (optionalBasket.isPresent()) {
            basket = optionalBasket.get();
        } else {
            Integer basketId = (Integer) map.get("basketId");
            if (basketId != null) {
                basketId = basket.getId();
                basket = basketRepository.getOne(basketId);
            }
            if (basket == null) {
                throw new Exception("BASKET NOT FOUND");
            }
        }

        Product product = productRepository.getOne(productId);
        if (product == null) {
            throw new Exception("PRODUCT NOT FOUND");
        }
        BasketProduct basketProduct = basketProductRepository.findFirstByProductIdAndBasketId(product, basket);
        if (basketProduct == null) {
            basketProduct = basketProductService.createAndAddBasketProduct(map, basket);
        } else {
            Float oldQuantity = basketProduct.getQuantity();
            oldQuantity += quantity;
            basketProduct.setQuantity(oldQuantity);
        }

        Map<String, Object> body = new HashMap<>();
        try {
            basketProductRepository.save(basketProduct);
        } catch (
                Exception ex) {
            body.put("success", "false");
            body.put("msg", ex.getMessage());
            return ResponseEntity.badRequest().body(body);
        }

        body.put("success", "true");
        return ResponseEntity.ok(body);
    }


}

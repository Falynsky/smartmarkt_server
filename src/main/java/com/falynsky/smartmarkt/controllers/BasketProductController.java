package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.exceptions.NotEnoughQuantity;
import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import com.falynsky.smartmarkt.services.BasketProductService;
import com.falynsky.smartmarkt.utils.ResponseUtils;
import com.falynsky.smartmarkt.utils.ResponseMapUtils;
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

    public BasketProductController(
            BasketProductRepository basketProductRepository,
            BasketRepository basketRepository,
            ProductRepository productRepository,
            AccountRepository accountRepository,
            UserRepository userRepository,
            BasketProductService basketProductService) {
        this.basketProductRepository = basketProductRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.basketProductService = basketProductService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/getBasketId")
    public Map<String, Object> getBasketIdByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        Map<String, Object> data = new HashMap<>();
        data.put("basketId", basket.getId());
        return ResponseMapUtils.buildResponse(data, true);

    }

    @GetMapping("/getSummary")
    public Map<String, Object> getBasketSummaryByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.getId());

        Map<String, Object> data = basketProductService.getSelectedBasketSummary(basketProductDTOS);
        return ResponseMapUtils.buildResponse(data, true);

    }

    @GetMapping("/getUserProducts")
    public Map<String, Object> getBasketProductsByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndNotClosedYet(basket.getId());
        List<Map<String, Object>> basketProductsData = basketProductService.getSelectedBasketProductsData(basketProductDTOS);
        return ResponseMapUtils.buildResponse(basketProductsData, true);

    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        String productName = "";
        try {
            productName = basketProductService.updateOrCreateBasketProduct(map, userToken);
        } catch (NotEnoughQuantity e) {
            return ResponseUtils.errorResponse(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtils.sendCorrectResponse("Dodano " + productName + " do koszyka.");
    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeOneSelectedProductFromBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        String productName;
        try {
            productName = basketProductService.removeProductFromBasket(map, userToken);
        } catch (Exception ex) {
            return ResponseUtils.errorResponse(ex.getMessage());
        }
        return ResponseUtils.sendCorrectResponse("Usunięto jedną pozycję produktu - " + productName);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/removeAllFromKind")
    public ResponseEntity<Map<String, Object>> removeSelectedProductsFromBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        String productName;
        try {
            productName = basketProductService.removeProductFromBasket(map, userToken);
        } catch (Exception ex) {
            return ResponseUtils.errorResponse(ex.getMessage());
        }
        return ResponseUtils.sendCorrectResponse("Usunięto wszystkie pozycję produktu - " + productName);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/removeAll")
    public ResponseEntity<Map<String, Object>> removeAllProductsFromBasket(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        try {
            basketProductService.removeAllProductsFromBasket(userToken);
        } catch (Exception ex) {
            return ResponseUtils.errorResponse(ex.getMessage());
        }
        return ResponseUtils.sendCorrectResponse("Koszyk został opróżniony");
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/purchaseAll")
    public ResponseEntity<Map<String, Object>> purchaseAllProductsFromBasket(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        try {
            basketProductService.purchaseAllProductsFromBasket(userToken);
        } catch (Exception ex) {
            return ResponseUtils.errorResponse(ex.getMessage());
        }
        return ResponseUtils.sendCorrectResponse("Produkty zostały zakupione");
    }
}

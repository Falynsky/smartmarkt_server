package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.exceptions.NotEnoughQuantity;
import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.*;
import com.falynsky.smartmarkt.services.BasketProductService;
import com.falynsky.smartmarkt.services.ResponseMsgService;
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


    @GetMapping("/getSummary")
    public Map<String, Object> getBasketSummaryByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketId(basket.getId());

        Map<String, Object> data = basketProductService.getSelectedBasketSummary(basketProductDTOS);

        return new HashMap<String, Object>() {
            {
                put("data", data);
                put("success", true);
            }
        };

    }

    @GetMapping("/getUserProducts")
    public Map<String, Object> getBasketProductsByUsername(
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {

        Basket basket = basketProductService.getUserBasketByUserToken(userToken);
        List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketId(basket.getId());
        List<Map<String, Object>> basketProductsData = basketProductService.getSelectedBasketProductsData(basketProductDTOS);

        return new HashMap<String, Object>() {
            {
                put("data", basketProductsData);
                put("success", true);
            }
        };

    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToBasket(
            @RequestBody Map<String, Object> map,
            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) {
        String productName = "";
        try {
            productName = basketProductService.updateOrCreateBasketProduct(map, userToken);
        } catch (NotEnoughQuantity e) {
            return ResponseMsgService.errorResponse(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMsgService.sendCorrectResponse("Dodano " + productName + " do koszyka.");
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
            return ResponseMsgService.errorResponse(ex.getMessage());
        }
        return ResponseMsgService.sendCorrectResponse("Produk został usunięty z koszyka");
    }
//
//    @SneakyThrows
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/removeOne")
//    public ResponseEntity<Map<String, Object>> removeOneFromBasket(
//            @RequestBody Map<String, Object> map,
//            @RequestHeader(value = "Auth", defaultValue = "empty") String userToken) throws Exception {
//        BasketProduct basketProduct = basketProductService.getBasketProduct(map, userToken);
//        if (basketProduct == null) {
//            return ResponseMsgService.elementNotFoundResponse();
//        }
//        try {
//            if (basketProduct.getQuantity() == 1) {
//                basketProductRepository.delete(basketProduct);
//            } else if (basketProduct.getQuantity() > 1) {
//                int newQuantity = basketProduct.getQuantity() - 1;
//                basketProduct.setQuantity(newQuantity);
//                basketProductRepository.save(basketProduct);
//            }
//        } catch (Exception ex) {
//            return ResponseMsgService.errorResponse(ex.getMessage());
//        }
//        return ResponseMsgService.sendCorrectResponse();
//    }

}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.BasketProduct;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.Product;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import com.falynsky.smartmarkt.services.BasketProductService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/baskets_products")
public class BasketProductController {

    BasketProductRepository basketProductRepository;
    BasketRepository basketRepository;
    ProductRepository productRepository;
    BasketProductService basketProductService;

    public BasketProductController(BasketProductRepository basketProductRepository,
                                   BasketRepository basketRepository,
                                   ProductRepository productRepository,
                                   BasketProductService basketProductService) {
        this.basketProductRepository = basketProductRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.basketProductService = basketProductService;
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

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    public void addToBasket(@RequestBody Map<String, Object> map) {
        Integer productId = (Integer) map.get("productId");
        Float quantity = ((Double) map.get("quantity")).floatValue();
        Integer basketId = (Integer) map.get("basketId");
        Product product = productRepository.getOne(productId);

        Basket basket = basketRepository.getOne(basketId);
        BasketProduct basketProduct = basketProductRepository.findFirstByProductIdAndBasketId(product, basket);
        BasketProduct oldBasketProduct;
        if (basketProduct == null) {
            basketProduct = basketProductService.createAndAddBasketProduct(map);
        } else {
            oldBasketProduct = basketProduct;
            Float oldQuantity = basketProduct.getQuantity();
            oldQuantity += quantity;
            basketProduct.setQuantity(oldQuantity);
        }
        basketProductRepository.save(basketProduct);

    }


}

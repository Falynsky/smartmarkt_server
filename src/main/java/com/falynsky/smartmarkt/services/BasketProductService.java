package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.BasketProduct;
import com.falynsky.smartmarkt.models.Product;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BasketProductService {

    BasketRepository basketRepository;
    BasketProductRepository basketProductRepository;
    ProductRepository productRepository;

    BasketProductService(BasketProductRepository basketProductRepository,
                         ProductRepository productRepository,
                         BasketRepository basketRepository) {
        this.basketProductRepository = basketProductRepository;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
    }

    public BasketProduct createAndAddBasketProduct(Map<String, Object> map, Basket basket) {
        BasketProduct basketProduct = new BasketProduct();

        Integer id = getIdForNewBasketProduct(basketProductRepository);
        basketProduct.setId(id);

        Float quantity = getQuantity(map);
        basketProduct.setQuantity(quantity);

        Product product = getProduct(map);
        basketProduct.setProductId(product);

//        Basket basket = getBasket(map);
        basketProduct.setBasketId(basket);

        basketProduct.setQuantityType("szt.");

        return basketProduct;
    }

    private Basket getBasket(Map<String, Object> map) {
        Integer basketId = (Integer) map.get("basketId");
        return basketRepository.getOne(basketId);
    }

    private Float getQuantity(Map<String, Object> map) {
        Object quantityValue =  map.get("quantity");

        Float quantity = null;
        if (quantityValue instanceof Double) {
            quantity = ((Double) quantityValue).floatValue();
        } else if (quantityValue instanceof Integer) {
            quantity = ((Integer) quantityValue).floatValue();
        }
        return quantity;
    }

    private Product getProduct(Map<String, Object> map) {
        Integer productId = (Integer) map.get("productId");
        return productRepository.getOne(productId);
    }

    private Integer getIdForNewBasketProduct(BasketProductRepository basketProductRepository) {
        BasketProduct lastBasketProduct = basketProductRepository.findFirstByOrderByIdDesc();
        if (lastBasketProduct == null) {
            return 1;
        }
        Integer lastId = lastBasketProduct.getId();
        return ++lastId;
    }
}

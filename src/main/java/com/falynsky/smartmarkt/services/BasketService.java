package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.objects.Basket;
import com.falynsky.smartmarkt.models.objects.User;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import org.springframework.stereotype.Service;

@Service
public class BasketService {

    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Basket getCurrentUserBasket(User user) throws Exception {
        Basket basket = basketRepository.findByUserId(user);
        if (basket == null) {
            throw new Exception("BASKET NOT FOUND");
        } else {
            return basket;
        }
    }
}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.BasketDTO;
import com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.repositories.BasketHistoryRepository;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.utils.PriceUtils;
import com.falynsky.smartmarkt.utils.ResponseMapBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/history")
public class HistoryController {

    private final BasketRepository basketRepository;
    private final BasketHistoryRepository basketHistoryRepository;
    private final BasketProductRepository basketProductRepository;

    public HistoryController(BasketRepository basketRepository, BasketHistoryRepository basketHistoryRepository, BasketProductRepository basketProductRepository) {
        this.basketRepository = basketRepository;
        this.basketHistoryRepository = basketHistoryRepository;
        this.basketProductRepository = basketProductRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/user/{userId}")
    public Map<String, Object> getProfileHistory(@PathVariable("userId") Integer userId) {
        Map<String, Object> data = new HashMap<>();
        try {
            BasketDTO basketDTO = basketRepository.retrieveBasketAsDTObyUserId(userId);
            int basketId = basketDTO.id;
            List<BasketHistoryDTO> basketHistoryDTOS = basketHistoryRepository.retrieveBasketsHistoryAsDTObyBasketId(basketId);
            List<Map<String, Object>> historyList = new ArrayList<>();
            for (BasketHistoryDTO basketHistoryDTO : basketHistoryDTOS) {
                Map<String, Object> basketHistoryData = new HashMap<>();
                int basketHistoryId = basketHistoryDTO.getId();
                List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndHistoryAndClosed(basketId, basketHistoryId);
                double purchasedPriceSummary = 0;
                String purchasedDateTime = null;
                for (BasketProductDTO basketProductDTO : basketProductDTOS) {
                    double fullQuantityPurchasedPriceSummary = basketProductDTO.getPurchasedPrice() * basketProductDTO.getQuantity();
                    purchasedPriceSummary += fullQuantityPurchasedPriceSummary;
                    if (purchasedDateTime == null) {
                        Date purchaseDateTime = basketProductDTO.getPurchaseDateTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        purchasedDateTime = dateFormat.format(purchaseDateTime);
                    }
                }

                basketHistoryData.put("purchasedPriceSummary", PriceUtils.roundPrice(purchasedPriceSummary));
                basketHistoryData.put("purchasedDateTime", purchasedDateTime);
                historyList.add(basketHistoryData);
            }
            data.put("historyList", historyList);
        } catch (Exception ex) {
            return ResponseMapBuilder.buildResponse(null, false);
        }
        return ResponseMapBuilder.buildResponse(data, true);
    }
}

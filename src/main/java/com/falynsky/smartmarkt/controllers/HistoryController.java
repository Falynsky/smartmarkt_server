package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.BasketDTO;
import com.falynsky.smartmarkt.models.DTO.BasketHistoryDTO;
import com.falynsky.smartmarkt.models.DTO.BasketProductDTO;
import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.repositories.BasketHistoryRepository;
import com.falynsky.smartmarkt.repositories.BasketProductRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.repositories.ProductRepository;
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
    private final ProductRepository productRepository;

    public HistoryController(BasketRepository basketRepository, BasketHistoryRepository basketHistoryRepository, BasketProductRepository basketProductRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.basketHistoryRepository = basketHistoryRepository;
        this.basketProductRepository = basketProductRepository;
        this.productRepository = productRepository;
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

                basketHistoryData.put("basketHistoryId", basketHistoryId);
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

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/{userId}/{basketHistoryId}")
    public Map<String, Object> getSelectedHistoryItems(@PathVariable("userId") Integer userId,
                                                       @PathVariable("basketHistoryId") Integer basketHistoryId) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> productsList = new ArrayList<>();
        try {
            BasketDTO basketDTO = basketRepository.retrieveBasketAsDTObyUserId(userId);
            int basketId = basketDTO.id;
            List<BasketProductDTO> basketProductDTOS = basketProductRepository.retrieveBasketProductAsDTObyBasketIdAndHistoryAndClosed(basketId, basketHistoryId);
            for (BasketProductDTO basketProductDTO : basketProductDTOS) {
                Map<String, Object> productData = new HashMap<>();
                double purchasedPrice = basketProductDTO.getPurchasedPrice();
                int quantity = basketProductDTO.getQuantity();
                int productId = basketProductDTO.getProductId();
                Optional<ProductDTO> optionalProductDTO = productRepository.retrieveProductAsDTObyId(productId);
                if (optionalProductDTO.isPresent()) {
                    ProductDTO productDTO = optionalProductDTO.get();
                    String productName = productDTO.getName();
                    productData.put("productName", productName);
                    productData.put("quantity", quantity);
                    productData.put("purchasedPrice", PriceUtils.roundPrice(purchasedPrice));
                    productsList.add(productData);
                }
            }
            data.put("productsList", productsList);
        } catch (Exception ex) {
            return ResponseMapBuilder.buildResponse(null, false);
        }
        return ResponseMapBuilder.buildResponse(data, true);
    }
}

package com.falynsky.smartmarkt.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketProductDTO {

    int id;
    int quantity;
    String quantityType;
    double weight;
    double purchasedPrice;
    Boolean purchased;
    Date purchaseDateTime;
    Boolean closed;
    int basketId;
    int productId;
}

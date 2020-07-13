package com.falynsky.smartmarkt.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketProductDTO {

    int id;
    int quantity;
    String quantityType;
    int basketId;
    int productId;
}

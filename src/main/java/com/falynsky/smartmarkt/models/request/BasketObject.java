package com.falynsky.smartmarkt.models.request;

public class BasketObject {

    public Integer productId;
    public Integer quantity;

    public BasketObject(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

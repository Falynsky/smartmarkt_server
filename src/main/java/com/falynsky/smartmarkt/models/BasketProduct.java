package com.falynsky.smartmarkt.models;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "baskets_products")
public class BasketProduct {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "quantity_type")
    String quantityType;

    @Column(name = "purchased")
    Boolean purchased;

    @Column(name = "closed")
    Boolean closed;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    Basket basketId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product productId;

    @ManyToOne(    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "baskets_history_id")
    BasketHistory basketsHistoryId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Basket getBasketId() {
        return basketId;
    }

    public void setBasketId(Basket basketId) {
        this.basketId = basketId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public BasketHistory getBasketsHistoryId() {
        return basketsHistoryId;
    }

    public void setBasketsHistoryId(BasketHistory basketsHistoryId) {
        this.basketsHistoryId = basketsHistoryId;
    }
}

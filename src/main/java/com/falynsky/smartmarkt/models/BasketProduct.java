package com.falynsky.smartmarkt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "weight")
    double weight;

    @Column(name = "purchased_price")
    double purchasedPrice;

    @Column(name = "purchased")
    Boolean purchased;

    @Column(name = "purchase_date_time")
    Date purchaseDateTime;

    @Column(name = "closed")
    Boolean closed;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    Basket basketId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product productId;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "baskets_history_id")
    BasketHistory basketsHistoryId;

}

package com.falynsky.smartmarkt.models;

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
    float quantity;

    @Column(name = "quantity_type")
    String quantityType;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    Basket basketId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product productId;

}

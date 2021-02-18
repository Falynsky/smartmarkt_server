package com.falynsky.smartmarkt.models.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "baskets_histories")
public class BasketHistory {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "purchased")
    Boolean purchased;

    @Column(name = "closed")
    Boolean closed;


    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "basket_id")
    Basket basketId;
}

package com.falynsky.smartmarkt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "description")
    public String description;
    @Column(name = "discount")
    public double discount;

    @OneToOne()
    @JoinColumn(name = "product_id")
    public Product productId;
}

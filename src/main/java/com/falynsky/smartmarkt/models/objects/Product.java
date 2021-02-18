package com.falynsky.smartmarkt.models.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(nullable = false)
    public int id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public int quantity;
    @Column(nullable = false)
    public double price;
    @Column
    public double weight;
    @Column(name = "product_type_id", nullable = false)
    public int productTypeId;
    @Column(name = "product_info", nullable = false)
    public String productInfo;
    @OneToOne
    @JoinColumn(name = "document_id")
    public Document documentId;

}


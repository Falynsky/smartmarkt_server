package com.falynsky.smartmarkt.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    public int id;
    public String name;
    public int quantity;
    public float price;
    public String currency;
    public int productTypeId;
    public String productInfo;

}


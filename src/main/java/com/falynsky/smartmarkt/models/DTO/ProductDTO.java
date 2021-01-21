package com.falynsky.smartmarkt.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    public int id;
    public String name;
    public int quantity;
    public double price;
    public double weight;
    public int productTypeId;
    public String productInfo;
    public Integer documentId;
    public String documentName;
    public String documentType;
}


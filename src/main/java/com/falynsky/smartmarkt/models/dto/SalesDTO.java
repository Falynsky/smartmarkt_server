package com.falynsky.smartmarkt.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesDTO {

    public int id;
    public String title;
    public String description;
    public double discount;
    public double originalPrice;
    public int productId;
    public String docName;
    public String docType;
}

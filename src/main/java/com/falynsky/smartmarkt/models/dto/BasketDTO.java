package com.falynsky.smartmarkt.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {

    public int id;
    public String name;
    public int userId;

}


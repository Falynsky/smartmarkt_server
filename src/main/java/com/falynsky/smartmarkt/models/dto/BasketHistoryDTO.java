package com.falynsky.smartmarkt.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketHistoryDTO {

    int id;
    Boolean purchased;
    Boolean closed;
    int basketId;
}

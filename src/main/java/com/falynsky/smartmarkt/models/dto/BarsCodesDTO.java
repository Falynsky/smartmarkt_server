package com.falynsky.smartmarkt.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarsCodesDTO {

    public int id;
    public int code;
    public int productId;

}


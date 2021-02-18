package com.falynsky.smartmarkt.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private int id;
    private String login;
    private String password;
    private String mail;
    private String role;

}

package com.falynsky.smartmarkt.models.request;

import com.falynsky.smartmarkt.models.objects.Account;
import lombok.Data;

@Data
public class RegisterObject extends Account {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String mail;

}

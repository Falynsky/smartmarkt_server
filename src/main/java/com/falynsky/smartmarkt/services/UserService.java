package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getCurrentUserByAccount(Account account) throws Exception {
        User user = userRepository.findFirstByAccountId(account);
        if (user == null) {
            throw new Exception("USER NOT FOUND");
        }
        return user;
    }

}

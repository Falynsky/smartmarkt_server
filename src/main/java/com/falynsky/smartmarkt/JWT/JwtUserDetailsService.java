package com.falynsky.smartmarkt.JWT;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.repositories.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class JwtUserDetailsService implements UserDetailsService {


    AccountRepository accountRepository;

    public JwtUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<Account> userData = accountRepository.findByUsername(username);
        return userData.orElse(null);
    }
}
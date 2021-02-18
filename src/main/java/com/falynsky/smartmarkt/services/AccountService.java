package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.JWT.utils.JwtTokenUtil;
import com.falynsky.smartmarkt.models.objects.Account;
import com.falynsky.smartmarkt.models.objects.Basket;
import com.falynsky.smartmarkt.models.objects.User;
import com.falynsky.smartmarkt.models.request.RegisterObject;
import com.falynsky.smartmarkt.repositories.AccountRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            BasketRepository basketRepository,
            PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Account getCurrentAccount(String userToken) throws Exception {
        String currentUserUsername = jwtTokenUtil.getUsernameFromToken(userToken.substring(5));

        Account account = accountRepository.findByUsername(currentUserUsername);
        if (account == null) {
            throw new Exception("ACCOUNT NOT FOUND");
        }
        return account;
    }

    public void createNewAccountData(RegisterObject registerObject, User user) {
        Account account = createNewAccount(registerObject);
        createNewUser(account, user);
        createNewUserBasket(registerObject, user);
    }

    private Account createNewAccount(RegisterObject registerObject) {
        Integer newAccountId = getIdForNewAccount();
        String username = registerObject.username;
        String encodePassword = getEncodedPassword(registerObject);
        String mail = registerObject.mail;
        String role = "ROLE_USER";

        Account newAccount = new Account(newAccountId, username, encodePassword, mail, role);
        accountRepository.save(newAccount);
        return newAccount;
    }

    private String getEncodedPassword(Account account) {
        String password = account.getPassword();
        return passwordEncoder.encode(password);
    }

    private Integer getIdForNewAccount() {
        Account lastAccount = accountRepository.findFirstByOrderByIdDesc();
        if (lastAccount == null) {
            return 1;
        }
        Integer lastId = lastAccount.getId();
        return ++lastId;
    }

    private void createNewUser(Account account, User user) {
        int newUserId = getIdForNewUser();
        user.setId(newUserId);
        user.setAccountId(account);
        userRepository.save(user);
    }

    private int getIdForNewUser() {
        User lastUser = userRepository.findFirstByOrderByIdDesc();
        if (lastUser == null) {
            return 1;
        }
        int lastId = lastUser.getId();
        return ++lastId;
    }

    private void createNewUserBasket(RegisterObject registerObject, User user) {
        int newBasketId = getIdForNewBasket();
        String userBasketName = registerObject.username.toUpperCase() + "_BASKET";
        Basket newUserBasket = new Basket(newBasketId, userBasketName, user);
        basketRepository.save(newUserBasket);
    }

    private int getIdForNewBasket() {
        Basket lastUser = basketRepository.findFirstByOrderByIdDesc();
        if (lastUser == null) {
            return 1;
        }
        int lastId = lastUser.getId();
        return ++lastId;
    }
}

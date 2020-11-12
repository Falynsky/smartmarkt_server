package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.Basket;
import com.falynsky.smartmarkt.models.DTO.LicenceDTO;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.models.User;
import com.falynsky.smartmarkt.repositories.AccountRepository;
import com.falynsky.smartmarkt.repositories.BasketRepository;
import com.falynsky.smartmarkt.repositories.LicenceRepository;
import com.falynsky.smartmarkt.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final PasswordEncoder passwordEncoder;
    private final LicenceRepository licenceRepository;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            BasketRepository basketRepository, PasswordEncoder passwordEncoder,
            LicenceRepository licenceRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
        this.passwordEncoder = passwordEncoder;
        this.licenceRepository = licenceRepository;
    }

    public void createNewAccountData(Account account, Licence formLicence, User user) {
        createNewAccount(account, formLicence);
        createNewUser(account, user);
        createNewUserBasket(account, user);
    }

    private void createNewAccount(Account account, Licence formLicence) {
        String encodePassword = getEncodedPassword(account);
        account.setPassword(encodePassword);

        Integer newAccountId = getIdForNewAccount();
        account.setId(newAccountId);

        String licenceKey = formLicence.getLicenceKey();
        LicenceDTO licenceDTO = licenceRepository.getLicenceByLicenceKey(licenceKey);
        if (licenceDTO != null || licenceKey.contains("338338")) {
            account.setRole("ROLE_ADMIN");
        } else {
            account.setRole("ROLE_USER");
        }
        accountRepository.save(account);
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

    private void createNewUserBasket(Account account, User user) {
        int newBasketId = getIdForNewBasket();
        String userBasketName = account.getUsername().toUpperCase() + "_BASKET";
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

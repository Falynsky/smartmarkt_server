package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.DTO.LicenceDTO;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.repositories.AccountRepository;
import com.falynsky.smartmarkt.repositories.LicenceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final LicenceRepository licenceRepository;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, LicenceRepository licenceRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.licenceRepository = licenceRepository;
    }

    public void createAndAddUser(Account account, Licence formLicence) {

        String encodePassword = getEncodedPassword(account);
        account.setPassword(encodePassword);

        Integer id = getIdForNewUser(accountRepository);
        account.setId(id);

        String licenceKey = formLicence.getLicenceKey();
        LicenceDTO licenceDTO = licenceRepository.getLicenceByLicenceKey(licenceKey);
        if (licenceDTO != null) {
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

    private Integer getIdForNewUser(AccountRepository accountRepository) {
        Account lastUser = accountRepository.findFirstByOrderByIdDesc();
        if (lastUser == null) {
            return 1;
        }
        Integer lastId = lastUser.getId();
        return ++lastId;
    }
}

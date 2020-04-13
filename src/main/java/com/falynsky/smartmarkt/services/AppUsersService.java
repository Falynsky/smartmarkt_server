package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUsersService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUsersService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAndAddUser(AppUsers appUsers) {

        String encodePassword = getEncodedPassword(appUsers);
        appUsers.setPassword(encodePassword);

        Integer id = getIdForNewUser(appUserRepository);
        appUsers.setId(id);

        appUsers.setRole("ROLE_USER");
        appUserRepository.save(appUsers);
    }

    private String getEncodedPassword(AppUsers appUsers) {
        String password = appUsers.getPassword();
        return passwordEncoder.encode(password);
    }

    private Integer getIdForNewUser(AppUserRepository appUserRepository) {
        AppUsers lastUser = appUserRepository.findFirstByOrderByIdDesc();
        if (lastUser == null) {
            return 1;
        }
        Integer lastId = lastUser.getId();
        return ++lastId;
    }
}

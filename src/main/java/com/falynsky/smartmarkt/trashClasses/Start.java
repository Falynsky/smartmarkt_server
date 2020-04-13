package com.falynsky.smartmarkt.trashClasses;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Start {

    private AppUserRepository appUserRepository;

    public Start(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;

        AppUsers appUsers = new AppUsers();
        Integer newId = getIdForNewUser(appUserRepository);
        appUsers.setId(newId);
        appUsers.setUsername("user" + newId);
        appUsers.setPassword(passwordEncoder.encode("user" + newId));
        appUsers.setRole("user");
        appUserRepository.save(appUsers);
    }

    private Integer getIdForNewUser(AppUserRepository appUserRepository) {
        AppUsers lastUser = appUserRepository.findFirstByOrderByIdDesc();
        Integer lastId = lastUser.getId();
        return ++lastId;
    }
}

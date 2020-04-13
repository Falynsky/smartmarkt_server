package com.falynsky.smartmarkt.trashClasses;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

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
        appUsers.setRole("ROLE_USER");
        appUserRepository.save(appUsers);


        AppUsers adminUser = new AppUsers();
        Integer adminId = getIdForNewUser(appUserRepository);
        adminUser.setId(adminId);
        adminUser.setUsername("user" + adminId);
        adminUser.setPassword(passwordEncoder.encode("user" + adminId));
        adminUser.setRole("ROLE_ADMIN");
        appUserRepository.save(adminUser);
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

package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.AppUser;
import com.falynsky.smartmarkt.models.DTO.LicenceDTO;
import com.falynsky.smartmarkt.models.Licence;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import com.falynsky.smartmarkt.repositories.LicenceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUsersService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private LicenceRepository licenceRepository;

    public AppUsersService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, LicenceRepository licenceRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.licenceRepository = licenceRepository;
    }

    public void createAndAddUser(AppUser appUser, Licence formLicence) {

        String encodePassword = getEncodedPassword(appUser);
        appUser.setPassword(encodePassword);

        Integer id = getIdForNewUser(appUserRepository);
        appUser.setId(id);

        String licenceKey = formLicence.getLicenceKey();
        LicenceDTO licenceDTO = licenceRepository.getLicenceByLicenceKey(licenceKey);
        if (licenceDTO != null) {
            appUser.setRole("ROLE_ADMIN");
        } else {
            appUser.setRole("ROLE_USER");
        }

        appUserRepository.save(appUser);
    }

    private String getEncodedPassword(AppUser appUser) {
        String password = appUser.getPassword();
        return passwordEncoder.encode(password);
    }

    private Integer getIdForNewUser(AppUserRepository appUserRepository) {
        AppUser lastUser = appUserRepository.findFirstByOrderByIdDesc();
        if (lastUser == null) {
            return 1;
        }
        Integer lastId = lastUser.getId();
        return ++lastId;
    }
}

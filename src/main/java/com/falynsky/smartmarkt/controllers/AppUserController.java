package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.AppUser;
import com.falynsky.smartmarkt.models.DTO.AppUserDTO;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class AppUserController {

    final AppUserRepository usersRepository;

    public AppUserController(AppUserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/all")
    public List<AppUserDTO> getAllUsers() {
        return usersRepository.retrieveAppUserAsDTO();
    }

    @GetMapping("/{id}")
    public AppUserDTO getAllUsers(@PathVariable("id") Integer id) {
        return usersRepository.retrieveAppUserAsDTObyId(id);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        Optional<AppUser> userToDelete = usersRepository.findById(userId);
        if (userToDelete.isPresent()) {
            usersRepository.delete(userToDelete.get());
            return "User deleted with id = " + userId;
        } else {
            return String.valueOf(new IOException("User with id = " + userId + " do not exists!"));
        }
    }

    @GetMapping("/mocked")
    public AppUserDTO getMockedUser() {
        AppUserDTO mockedUser = new AppUserDTO(1, "mockedUser", "mockedUser", "user");
        return mockedUser;
    }

    @GetMapping("/mocked/{numberOfUsers}")
    public List<AppUserDTO> getAllMockedUsers(@PathVariable Integer numberOfUsers) {
        List<AppUserDTO> mockedUsers =
                generateUsers(numberOfUsers);
        return mockedUsers;
    }

    private List<AppUserDTO> generateUsers(int numberOfUsers) {
        List<AppUserDTO> users = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            users.add(new AppUserDTO(i, "user" + i, "user" + i, "user"));
        }
        return users;
    }
}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.models.DTO.AppUsersDTO;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class AppUsersController {

    final AppUserRepository usersRepository;

    public AppUsersController(AppUserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/all")
    public List<AppUsersDTO> getAllUsers() {
        return usersRepository.retrieveAppUsersAsDTO();
    }

    @GetMapping("/{id}")
    public AppUsersDTO getAllUsers(@PathVariable("id") Integer id) {
        return usersRepository.retrieveAppUsersAsDTObyId(id);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        Optional<AppUsers> userToDelete = usersRepository.findById(userId);
        if (userToDelete.isPresent()) {
            usersRepository.delete(userToDelete.get());
            return "User deleted with id = " + userId;
        } else {
            return String.valueOf(new IOException("User with id = " + userId + " do not exists!"));
        }
    }

    @GetMapping("/mocked")
    public AppUsersDTO getMockedUser() {
        AppUsersDTO mockedUser = new AppUsersDTO(1, "mockedUser", "mockedUser", "user");
        return mockedUser;
    }

    @GetMapping("/mocked/{numberOfUsers}")
    public List<AppUsersDTO> getAllMockedUsers(@PathVariable Integer numberOfUsers) {
        List<AppUsersDTO> mockedUsers =
                generateUsers(numberOfUsers);
        return mockedUsers;
    }

    private List<AppUsersDTO> generateUsers(int numberOfUsers) {
        List<AppUsersDTO> users = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            users.add(new AppUsersDTO(i, "user" + i, "user" + i, "user"));
        }
        return users;
    }
}

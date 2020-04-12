package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.UsersDTO;
import com.falynsky.smartmarkt.models.Users;
import com.falynsky.smartmarkt.repositories.UsersRepository;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UsersController {

    final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/all")
    public List<UsersDTO> getAllUsers() {
        return usersRepository.retrieveUsersAsDTO();
    }

    @GetMapping("/{id}")
    public UsersDTO getAllUsers(@PathVariable("id") Integer id) {
        return usersRepository.retrieveUsersAsDTObyId(id);
    }

    @RequestMapping("/create")
    public void addNewUser() {
        UsersDTO user = new UsersDTO(4, "test4", "test");
        Users usersEntity = new Users(user);
        usersRepository.save(usersEntity);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        Optional<Users> userToDelete = usersRepository.findById(userId);
        if (userToDelete.isPresent()) {
            usersRepository.delete(userToDelete.get());
            return "User deleted with id = " + userId;
        } else {
            return String.valueOf(new IOException("User with id = " + userId + " do not exists!"));
        }
    }

    @GetMapping("/mocked")
    public UsersDTO getMockedUser() {
        UsersDTO mockedUser = new UsersDTO(1, "mockedUser", "mockedUser");
        Gson gson = new Gson();
        String json = gson.toJson(mockedUser);
        return mockedUser;
    }

    @GetMapping("/mocked/{numberOfUsers}")
    public List<UsersDTO> getAllMockedUsers(@PathVariable Integer numberOfUsers) {
        List<UsersDTO> mockedUsers =
                generateUsers(numberOfUsers);
        return mockedUsers;
    }

    private List<UsersDTO> generateUsers(int numberOfUsers) {
        List<UsersDTO> users = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            users.add(new UsersDTO(i, "user" + i, "user" + i));
        }
        return users;
    }
}

package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.Users;
import com.falynsky.smartmarkt.models.UsersEntity;
import com.falynsky.smartmarkt.repositories.UsersRepository;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.google.gson.Gson;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<Users> getAllUsers() {
        return usersRepository.retrieveUsersAsDTO();
    }

    @GetMapping("/{id}")
    public Users getAllUsers(@PathVariable("id") Integer id) {
        return usersRepository.retrieveUsersAsDTObyId(id);
    }

    @RequestMapping("/create")
    public void addNewUser() {
        Users user = new Users(4, "test4", "test");
        UsersEntity usersEntity = new UsersEntity(user);
        usersRepository.save(usersEntity);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        Optional<UsersEntity> userToDelete = usersRepository.findById(userId);
        if (userToDelete.isPresent()) {
            usersRepository.delete(userToDelete.get());
            return "User deleted with id = " + userId;
        } else {
            return String.valueOf(new IOException("User with id = " + userId + " do not exists!"));
        }
    }

    @GetMapping("/mocked")
    public Users getMockedUser() {
        Users mockedUser = new Users(1, "mockedUser", "mockedUser");
        Gson gson = new Gson();
        String json = gson.toJson(mockedUser);
        return mockedUser;
    }

    @GetMapping("/mocked/{numberOfUsers}")
    public List<Users> getAllMockedUsers(@PathVariable Integer numberOfUsers) {
        List<Users> mockedUsers =
                generateUsers(numberOfUsers);
        return mockedUsers;
    }

    private List<Users> generateUsers(int numberOfUsers) {
        List<Users> users = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            users.add(new Users(i, "user" + i, "user" + i));
        }
        return users;
    }
}

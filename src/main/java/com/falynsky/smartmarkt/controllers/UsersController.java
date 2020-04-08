package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.DTO.Users;
import com.falynsky.smartmarkt.models.UsersEntity;
import com.falynsky.smartmarkt.repositories.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public void addNewUser(){
        Users user = new Users(4,"test", "test");
        UsersEntity usersEntity = new UsersEntity(user);
        usersRepository.save(usersEntity);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        Optional<UsersEntity> userToDelete = usersRepository.findById(userId);
        if (userToDelete.isPresent()) {
            usersRepository.delete(userToDelete.get());
            return "User deleted with id = " + userId;
        }
        else {
            return String.valueOf(new IOException("User with id = " + userId + " do not exists!"));
        }
    }

    ;
}

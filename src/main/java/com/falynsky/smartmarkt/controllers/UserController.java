package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.dto.UserDTO;
import com.falynsky.smartmarkt.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    final private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Integer id) {
        return userRepository.retrieveUserAsDTObyId(id);
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userRepository.retrieveUserAsDTO();
    }
}

package ru.sbrf.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbrf.lab.model.User;
import ru.sbrf.lab.service.UserService;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    UserService iUsersService;
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = iUsersService.getAllUsers();
        return users;
//        return getAllUsers();
    }
}


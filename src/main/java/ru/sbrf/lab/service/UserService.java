package ru.sbrf.lab.service;

import ru.sbrf.lab.model.User;
import java.util.List;


public interface UserService {
    User findUserById(long id);
    List<User> getAllUsers();
    User createUser (User user);
    User updateUser (User newUser, long id);
    void deleteUser(long id);
}

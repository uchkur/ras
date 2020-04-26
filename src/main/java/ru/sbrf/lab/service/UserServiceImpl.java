package ru.sbrf.lab.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.lab.exception.MissingRequiredFieldException;
import ru.sbrf.lab.exception.UserNotFoundException;
import ru.sbrf.lab.model.User;
import ru.sbrf.lab.repository.UserRepository;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User findUserById(long id)
    {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User with id \"" + id + "\" could not been found.");
        }

    }


    @Override
    @Transactional
    public User createUser (User user)
    {
        userRepository.save(user);
        return user;

    }

    @Override
    public void deleteUser(long id) {
        try {userRepository.deleteById(id); } catch
                (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User with id " + id + " could not been found.");
        }

    }

    @Override
    public User updateUser(User newUser, long id)
    {
        User user = findUserById(id);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}



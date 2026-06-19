package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameWithRoles(String username);

    Optional<User> findById(Long id);

    User save(User existingUser);

    boolean deleteById(Long id);

    List<User> findAll();
}

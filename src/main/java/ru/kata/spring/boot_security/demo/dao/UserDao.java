package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameWithRoles(String username);

    Optional<User> findById(Long id);

    void save(User existingUser);

    void deleteById(Long id);

    List<User> findAll();
}

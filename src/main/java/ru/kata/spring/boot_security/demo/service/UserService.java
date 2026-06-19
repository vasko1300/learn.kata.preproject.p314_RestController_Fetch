package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User updateProfile(User user);
    User saveUser(User user);
    boolean deleteById(Long id);
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
}

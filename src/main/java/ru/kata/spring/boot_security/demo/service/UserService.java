package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void updateProfile(User user);
    void saveUser(User user, List<Long> roleIds);
    void deleteById(Long id);
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
}

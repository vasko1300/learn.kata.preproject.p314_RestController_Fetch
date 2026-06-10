package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao {
    List<Role> findAll();
    void save(Role role);
    void deleteById(Long id);
    Optional<Role> findById(Long id);
}

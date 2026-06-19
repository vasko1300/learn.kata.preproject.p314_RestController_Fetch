package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao {
    List<Role> findAll();
    Role save(Role role);
    boolean deleteById(Long id);
    Optional<Role> findById(Long id);
}

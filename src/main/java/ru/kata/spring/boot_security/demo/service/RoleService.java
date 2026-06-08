package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface RoleService {
    void saveRole(Role role);
    void deleteRole(Long id);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
}

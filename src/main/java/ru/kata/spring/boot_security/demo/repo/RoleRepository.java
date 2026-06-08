package ru.kata.spring.boot_security.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    List<Role> findAll();
    void save(Role role);
    void deleteById(Long id);
    Optional<Role> findById(Long id);
}

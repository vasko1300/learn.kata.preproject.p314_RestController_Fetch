package ru.kata.spring.boot_security.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {}

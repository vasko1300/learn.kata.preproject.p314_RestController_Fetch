package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.domain.entity.Role;
import ru.kata.spring.boot_security.demo.domain.dto.RoleDto;
import ru.kata.spring.boot_security.demo.domain.dto.RoleEditDto;

import java.util.List;

public interface RoleService {
    RoleDto save(RoleDto roleDto);
    RoleDto edit(Long id, RoleEditDto roleEditDto);
    void deleteById(Long id);
    List<RoleDto> findAll();

    @Transactional(readOnly = true)
    RoleDto findRoleDtoByName(String name);

    Role findById(Long id);
    RoleDto findRoleDtoById(Long id);

    @Transactional(readOnly = true)
    Role findByName(String name);

    @Transactional(readOnly = true)
    List<String> principalRoleNames(Authentication auth);
}

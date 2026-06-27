package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.domain.Role;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.RoleEditDto;

import java.util.List;

public interface RoleService {
    RoleDto save(RoleDto roleDto);
    RoleDto edit(Long id, RoleEditDto roleEditDto);
    void deleteById(Long id);
    List<RoleDto> findAll();
    Role findById(Long id);
    RoleDto findRoleDtoById(Long id);
}

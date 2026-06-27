package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.domain.Role;

import java.util.Optional;

@Component
public class RoleMapper {
    public RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

    public Role toEntity(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.name());
        return role;
    }
}

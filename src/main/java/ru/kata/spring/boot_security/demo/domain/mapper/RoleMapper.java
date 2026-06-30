package ru.kata.spring.boot_security.demo.domain.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.domain.dto.RoleDto;
import ru.kata.spring.boot_security.demo.domain.entity.Role;

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

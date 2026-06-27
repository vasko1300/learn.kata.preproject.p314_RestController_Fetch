package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.domain.Role;
import ru.kata.spring.boot_security.demo.domain.User;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserEditDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final RoleService roleService;

    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                null,
                user.getFirstName(),
                user.getSecondName(),
                user.getBirthYear(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getRoles()
                        .stream().map(Role::getId).toList()
        );
    }
    public User dtoToEntity(UserCreateDto dto) {
        return new User(
                dto.username(),
                dto.password(),
                dto.firstName(),
                dto.secondName(),
                dto.birthYear(),
                dto.accountNonExpired(),
                dto.accountNonLocked(),
                dto.credentialsNonExpired(),
                dto.enabled(),
                dto.roleIds().stream().map(roleService::findById).collect(Collectors.toSet())
        );
    }
}
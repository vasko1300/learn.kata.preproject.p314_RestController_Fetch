package ru.kata.spring.boot_security.demo.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserCreateDto(
        String username,
        String password,
        String firstName,
        String secondName,
        Integer birthYear,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired,
        Boolean enabled,
        List<String> roleNames
) {}
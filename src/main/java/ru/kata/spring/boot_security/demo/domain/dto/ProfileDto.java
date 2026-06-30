package ru.kata.spring.boot_security.demo.domain.dto;

import java.util.List;

public record ProfileDto(
        Long id,
        String username,
        String firstName,
        String secondName,
        Integer birthYear,
        List<String> roles
) implements UserInfo {
    public static ProfileDto empty() {
        return new ProfileDto(null, null, null, null, null, null);
    }
}
package ru.kata.spring.boot_security.demo.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id,
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
) implements UserInfo {
    public static UserDto empty() {
        return new UserDto(null, null, null, null, null, null, null, null, null, null, null);
    }
}
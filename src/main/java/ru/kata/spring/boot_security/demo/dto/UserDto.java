package ru.kata.spring.boot_security.demo.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id,
                      String username,
                      String password,
                      String firstName,
                      String secondName,
                      Integer birthYear,
                      boolean accountNonExpired,
                      boolean accountNonLocked,
                      boolean credentialsNonExpired,
                      boolean enabled,
                      List<Long> roles
) {}
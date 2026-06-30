package ru.kata.spring.boot_security.demo.domain.dto;

import java.util.List;
import java.util.Optional;

public record UserEditDto(Optional<String> username,
                          Optional<String> password,
                          Optional<String> firstName,
                          Optional<String> secondName,
                          Optional<Integer> birthYear,
                          Optional<Boolean> accountNonExpired,
                          Optional<Boolean> accountNonLocked,
                          Optional<Boolean> credentialsNonExpired,
                          Optional<Boolean> enabled,
                          Optional<List<String>> roleNames
) {}
package ru.kata.spring.boot_security.demo.dto;

import java.util.List;
import java.util.Optional;

public record UserEditDto(Optional<String> username,
                          Optional<String> password,
                          Optional<String> firstName,
                          Optional<String> secondName,
                          Optional<Integer> birthYear,
// Optional<Boolean> - клиент должен определить логику менял ли пользователь поля
// (в форме должны быть предзаполнены текущим состоянием
                          Optional<Boolean> accountNonExpired,
                          Optional<Boolean> accountNonLocked,
                          Optional<Boolean> credentialsNonExpired,
                          Optional<Boolean> enabled,
                          Optional<List<Long>> roleIds
) {}
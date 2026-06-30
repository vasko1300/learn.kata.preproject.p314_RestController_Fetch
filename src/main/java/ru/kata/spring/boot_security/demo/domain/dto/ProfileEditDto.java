package ru.kata.spring.boot_security.demo.domain.dto;

import javax.validation.constraints.Size;
import java.util.Optional;

public record ProfileEditDto(
        Optional<String> firstName,
        Optional<String> secondName,
        Optional<Integer> birthYear,
//        @Size(min = 6, message = "Password must be at least 6 characters")
        Optional<String> password) {}
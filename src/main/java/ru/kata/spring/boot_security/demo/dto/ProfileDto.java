package ru.kata.spring.boot_security.demo.dto;

public record ProfileDto(Long id, String firstName, String secondName, Integer birthYear, java.util.List<Long> roles) {}

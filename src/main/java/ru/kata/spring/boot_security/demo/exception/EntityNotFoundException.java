package ru.kata.spring.boot_security.demo.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entityName;
    private final String searchKey;
    private final Object searchValue;
    public EntityNotFoundException(String entityName, String searchKey, Object searchValue) {
        super(String.format("%s with %s '%s' not found", entityName, searchKey, searchValue));
        this.entityName = entityName;
        this.searchKey = searchKey;
        this.searchValue = searchValue;
    }
}

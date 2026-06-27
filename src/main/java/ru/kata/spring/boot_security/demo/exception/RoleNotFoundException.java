package ru.kata.spring.boot_security.demo.exception;

public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(String searchKey, Object searchValue) {
        super("Role", searchKey, searchValue);
    }
}

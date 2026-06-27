package ru.kata.spring.boot_security.demo.exception;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String searchKey, Object searchValue) {
        super("User", searchKey, searchValue);
    }
}

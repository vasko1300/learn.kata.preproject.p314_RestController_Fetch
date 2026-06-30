package ru.kata.spring.boot_security.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(String searchKey, Object searchValue) {
        super("Role", searchKey, searchValue);
    }
}

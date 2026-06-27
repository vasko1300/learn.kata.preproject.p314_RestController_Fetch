package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.dto.ProfileEditDto;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserEditDto;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.mapper.ProfileMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
public class UserRestController {
    private final UserService userService;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, ProfileMapper profileMapper, UserMapper userMapper) {
        this.userService = userService;
        this.profileMapper = profileMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("profile")
    public ResponseEntity<ProfileDto> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.findProfileDtoByUsername(auth.getName()));
    }
    @PatchMapping("profile")
    public ResponseEntity<ProfileDto> editProfile(@Valid @RequestBody ProfileEditDto profileEditDto, Authentication auth) {
        ProfileDto updatedUser = userService.editProfile(auth.getName(), profileEditDto);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUserDto());
    }
    @GetMapping("users/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserDtoById(id));
    }
    @PostMapping("users/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/api/users/" + createdUser.id())
                .body(createdUser);
    }
    @PatchMapping("users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> editUser(@PathVariable Long id, @Valid @RequestBody UserEditDto userEditDto) {
        UserDto editedUser = userService.editUser(id, userEditDto);
        return ResponseEntity.ok(editedUser);
    }
    @DeleteMapping("users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
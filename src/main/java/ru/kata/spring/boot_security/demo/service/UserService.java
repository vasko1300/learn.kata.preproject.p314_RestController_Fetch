package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.domain.entity.User;
import ru.kata.spring.boot_security.demo.domain.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.domain.dto.UserDto;
import ru.kata.spring.boot_security.demo.domain.dto.UserEditDto;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileEditDto;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    ProfileDto findProfileDtoByUsername(String username);
    ProfileDto editProfile(String username, ProfileEditDto profileEditDto);

    User findById(Long id);
    UserDto findUserDtoById(Long id);
    List<UserDto> findAllUserDto();

    UserDto createUser(UserCreateDto userCreateDto);
    UserDto editUser(Long id, UserEditDto userEditDto);
    void deleteById(Long id);
}

package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.domain.User;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserEditDto;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.dto.ProfileEditDto;

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

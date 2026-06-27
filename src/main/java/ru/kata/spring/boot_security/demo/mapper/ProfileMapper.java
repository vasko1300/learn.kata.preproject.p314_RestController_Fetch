package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.domain.Role;
import ru.kata.spring.boot_security.demo.domain.User;

@Component
public class ProfileMapper {
    public ProfileDto toDto(User user) {
        return new ProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getSecondName(),
                user.getBirthYear(),
                user.getRoles()
                        .stream().map(Role::getId).toList()
        );
    }
    /*public User toEntity(UserProfileDto dto) {

    }*/
}

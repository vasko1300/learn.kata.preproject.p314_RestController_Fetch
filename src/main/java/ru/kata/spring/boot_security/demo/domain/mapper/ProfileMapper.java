package ru.kata.spring.boot_security.demo.domain.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.domain.entity.Role;
import ru.kata.spring.boot_security.demo.domain.entity.User;

@Component
public class ProfileMapper {
    public ProfileDto toDto(User user) {
        return new ProfileDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getSecondName(),
                user.getBirthYear(),
                user.getRoles()
                        .stream().map(role -> role.getName().replace("ROLE_", "")).toList()
        );
    }
    /*public User toEntity(UserProfileDto dto) {

    }*/
}

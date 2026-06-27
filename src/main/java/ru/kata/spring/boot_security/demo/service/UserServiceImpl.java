package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.domain.User;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserEditDto;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.dto.ProfileEditDto;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.mapper.ProfileMapper;
import ru.kata.spring.boot_security.demo.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, RoleService roleService, UserMapper userMapper, ProfileMapper profileMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("USERNAME", username));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto findProfileDtoByUsername(String username) {
        return profileMapper.toDto(findByUsername(username));
    }

    @Override
    public ProfileDto editProfile(String username, ProfileEditDto profileEditDto) {
        User existingUser = findByUsername(username);
        profileEditDto.firstName().ifPresent(existingUser::setFirstName);
        profileEditDto.secondName().ifPresent(existingUser::setSecondName);
        profileEditDto.birthYear().ifPresent(existingUser::setBirthYear);
        profileEditDto.password().ifPresent(pass -> existingUser.setPassword(passwordEncoder.encode(pass)));
        return profileMapper.toDto(existingUser); // merge произойдет автоматически в рамках транзакции
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("ID", id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserDtoById(Long id) {
        return userMapper.toDto(
                userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("ID", id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUserDto() {
        return userRepo.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User newUser = userMapper.dtoToEntity(userCreateDto);
        newUser.setPassword(passwordEncoder.encode(userCreateDto.password()));
        return userMapper.toDto(userRepo.save(newUser));
    }

    @Override
    public UserDto editUser(Long id, UserEditDto userEditDto) {
        User existingUser = findById(id);
        userEditDto.username().ifPresent(existingUser::setUsername);
        userEditDto.password().ifPresent(pass -> existingUser.setPassword(passwordEncoder.encode(pass)));
        userEditDto.firstName().ifPresent(existingUser::setFirstName);
        userEditDto.secondName().ifPresent(existingUser::setSecondName);
        userEditDto.birthYear().ifPresent(existingUser::setBirthYear);
        userEditDto.accountNonExpired().ifPresent(existingUser::setAccountNonExpired);
        userEditDto.accountNonLocked().ifPresent(existingUser::setAccountNonLocked);
        userEditDto.credentialsNonExpired().ifPresent(existingUser::setCredentialsNonExpired);
        userEditDto.enabled().ifPresent(existingUser::setEnabled);
        userEditDto.roleIds().ifPresent(
                roleIds -> existingUser.setRoles(
                        roleIds.stream().map(roleService::findById).collect(Collectors.toSet())
                )
        );
        return userMapper.toDto(existingUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}
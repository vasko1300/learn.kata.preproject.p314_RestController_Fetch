package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repo.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User updateProfile(User user) {
        User existingUser = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setSecondName(user.getSecondName());
        existingUser.setBirthYear(user.getBirthYear());

        return userRepo.save(existingUser);
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() != null && user.getId() > 0) { // Существующий пользователь
            User existingUser = userRepo.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setSecondName(user.getSecondName());
            existingUser.setBirthYear(user.getBirthYear());
            existingUser.setAccountNonExpired(user.isAccountNonExpired());
            existingUser.setAccountNonLocked(user.isAccountNonLocked());
            existingUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
            existingUser.setEnabled(user.isEnabled());
            existingUser.setRoles(user.getRoles());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepo.save(existingUser);
        } else { // Новый пользователь
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password is required for new user");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(user.getRoles());
            return userRepo.save(user);
        }
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
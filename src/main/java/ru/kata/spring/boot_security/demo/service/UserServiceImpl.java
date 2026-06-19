package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User updateProfile(User user) {
        User existingUser = userDao.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setSecondName(user.getSecondName());
        existingUser.setBirthYear(user.getBirthYear());

        return userDao.save(existingUser);
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() != null && user.getId() > 0) { // Существующий пользователь
            User existingUser = userDao.findById(user.getId())
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
            return userDao.save(existingUser);
        } else { // Новый пользователь
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password is required for new user");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(user.getRoles());
            return userDao.save(user);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        return userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
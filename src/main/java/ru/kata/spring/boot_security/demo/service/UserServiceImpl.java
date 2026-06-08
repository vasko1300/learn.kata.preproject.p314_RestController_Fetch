package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;
import ru.kata.spring.boot_security.demo.repo.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void saveUser(User user, String requestor) {
        if (user.getId() != null && user.getId() > 0) {
            // ========== РЕДАКТИРОВАНИЕ СУЩЕСТВУЮЩЕГО ПОЛЬЗОВАТЕЛЯ ==========
            User existingUser = userRepo.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

            // Поля, изменение которых доступно обеим ролям
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setSecondName(user.getSecondName());
            existingUser.setBirthYear(user.getBirthYear());

            // Поля, изменение которых доступно только админу
            if (requestor.equals("admin")) {
                existingUser.setAccountNonExpired(user.isAccountNonExpired());
                existingUser.setAccountNonLocked(user.isAccountNonLocked());
                existingUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
                existingUser.setEnabled(user.isEnabled());

                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                if (user.getRoles() != null) {
                    Set<Role> managedRoles = new HashSet<>();
                    for (Role role : user.getRoles()) {
                        if (role.getId() != null) {
                            managedRoles.add(roleRepo.findById(role.getId())
                                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getId())));
                        }
                    }
                    existingUser.setRoles(managedRoles);
                }
            }
            userRepo.save(existingUser);
            return;
        }

        // ========== СОЗДАНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ ==========
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required for new user");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Загружаем управляемые объекты ролей
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Role> managedRoles = new HashSet<>();
            for (Role role : user.getRoles()) {
                if (role.getId() != null) {
                    managedRoles.add(roleRepo.findById(role.getId())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role.getId())));
                }
            }
            user.setRoles(managedRoles);
        }

        userRepo.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
/*
package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;
import ru.kata.spring.boot_security.demo.repo.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepo;
    @Mock
    RoleRepository roleRepo;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    private User createTestUser() {
        String currentRawPass = "currentRawPass";
        return new User(
                "Bob345",
                currentRawPass,
                "Bob",
                "Ivanov",
                1984,
                true,
                true,
                true,
                true,
                Set.of(new Role(1L, "ROLE_USER")));
    }

    // ==================== ТЕСТЫ ДЛЯ saveUser ====================
    @Test
    @DisplayName("Should encode password and save new user")
    void saveUser_newUser_shouldEncodePasswordAndSave() {
        User unsavedUser = createTestUser();
        String rawPass = unsavedUser.getPassword();
        System.out.println("rawPass: " + rawPass);
        String encodedPass = "encodedPass";
        when(passwordEncoder.encode(rawPass)).thenReturn(encodedPass);
        when(userRepo.save(any(User.class))).thenReturn(unsavedUser);

        User savedUser = userService.saveUser(unsavedUser);
        verify(passwordEncoder).encode(rawPass);
        verify(userRepo, times(1)).save(any(User.class));

        assertThat(savedUser.getPassword()).isEqualTo(encodedPass);
        assertThat(savedUser).isEqualTo(unsavedUser);
    }

    @Test
    @DisplayName("Should throw exception when saving new user with null password")
    void saveUser_newUserWithoutPassword_shouldThrowException() {
        User newUser = createTestUser();
        newUser.setPassword(null);
        assertThatThrownBy(() -> userService.saveUser(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password is required for new user");
        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should throw exception when saving new user with empty password")
    void saveUser_newUserWithEmptyPassword_shouldThrowException() {
        User newUser = createTestUser();
        newUser.setPassword("");
        assertThatThrownBy(() -> userService.saveUser(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password is required for new user");
        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should update existing user with new password")
    void saveUser_existingUserWithNewPassword_shouldEncodeAndUpdate() {
        Long userId = 1L;
        String newRawPassword = "newRawPassword";
        String encodedNewPass = "encodedNewPass";
        String newName = "newName";

        User existingUser = createTestUser();
        existingUser.setId(userId);

        User updatedUser = createTestUser();
        updatedUser.setId(userId);
        updatedUser.setPassword(newRawPassword);
        updatedUser.setFirstName(newName);

        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newRawPassword)).thenReturn(encodedNewPass);
        when(userRepo.save(any(User.class))).thenReturn(existingUser);

        userService.saveUser(updatedUser);

        verify(passwordEncoder).encode(newRawPassword);
        verify(userRepo).save(any(User.class));

        assertThat(existingUser.getFirstName()).isEqualTo(newName);
        assertThat(existingUser.getPassword()).isEqualTo(encodedNewPass);
    }

    @Test
    @DisplayName("Should update existing user without changing password")
    void saveUser_existingUserWithoutPassword_shouldUpdateWithoutEncoding() {
        Long userId = 1L;
        User existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldEncodedPassword");

        User updatedUser = createTestUser();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword(null);

        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(existingUser)).thenReturn(existingUser);

        userService.saveUser(updatedUser);

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo).save(existingUser);
        assertThat(existingUser.getUsername()).isEqualTo("newUsername");
        assertThat(existingUser.getPassword()).isEqualTo("oldEncodedPassword");
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void saveUser_nonExistingUser_shouldThrowException() {
        Long nonExistentId = 999L;
        User user = createTestUser();
        user.setId(nonExistentId);

        when(userRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.saveUser(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: " + nonExistentId);

        verify(userRepo, never()).save(any(User.class));
    }

    // ==================== ТЕСТЫ ДЛЯ updateProfile ====================
    @Test
    @DisplayName("Should update profile fields without changing password")
    void updateProfile_shouldUpdateOnlyAllowedFields() {
        Long userId = 1L;
        User existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldEncodedPassword");

        User updatedProfile = createTestUser();
        updatedProfile.setId(userId);
        updatedProfile.setUsername("newUsername");
        updatedProfile.setFirstName("NewFirst");
        updatedProfile.setSecondName("NewSecond");
        updatedProfile.setBirthYear(2000);

        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.updateProfile(updatedProfile);

        // Assert
        verify(userRepo).save(existingUser);
        assertThat(result.getUsername()).isEqualTo("newUsername");
        assertThat(result.getFirstName()).isEqualTo("NewFirst");
        assertThat(result.getSecondName()).isEqualTo("NewSecond");
        assertThat(result.getBirthYear()).isEqualTo(2000);
        assertThat(result.getPassword()).isEqualTo("oldEncodedPassword");
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user profile")
    void updateProfile_nonExistingUser_shouldThrowException() {
        Long nonExistentId = 999L;
        User user = createTestUser();
        user.setId(nonExistentId);

        when(userRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateProfile(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: " + nonExistentId);

        verify(userRepo, never()).save(any(User.class));
    }

    // ==================== ТЕСТЫ ДЛЯ deleteById ====================
    @Test
    @DisplayName("Should delegate delete to repository")
    void deleteById_shouldCallRepositoryDelete() {
        Long userId = 1L;
        doNothing().when(userRepo).deleteById(userId);

        userService.deleteById(userId);

        verify(userRepo, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should not throw exception when deleting non-existing user")
    void deleteById_nonExistingUser_shouldNotThrowException() {
        Long nonExistentId = 999L;
        doNothing().when(userRepo).deleteById(nonExistentId);

        userService.deleteById(nonExistentId);

        assertThatCode(() -> userService.deleteById(nonExistentId))
                .doesNotThrowAnyException();

        verify(userRepo, times(2)).deleteById(nonExistentId);
    }

    @Test
    @DisplayName("Should handle null ID gracefully")
    void deleteById_nullId_shouldNotThrowException() {
        Long userId = null;
        doNothing().when(userRepo).deleteById(userId);

        assertThatCode(() -> userService.deleteById(userId))
                .doesNotThrowAnyException();

        verify(userRepo, times(1)).deleteById(userId);
    }

    // ==================== ТЕСТЫ ДЛЯ findAll ====================
    @Test
    @DisplayName("Should return list of all users")
    void findAll_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User(), new User());
        when(userRepo.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(users);
        verify(userRepo).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void findAll_noUsers_shouldReturnEmptyList() {
        when(userRepo.findAll()).thenReturn(List.of());

        List<User> result = userService.findAll();

        assertThat(result).isEmpty();
        verify(userRepo).findAll();
    }

    // ==================== ТЕСТЫ ДЛЯ findById ====================
    @Test
    @DisplayName("Should return user when exists")
    void findById_existingUser_shouldReturnUser() {
        Long userId = 1L;
        User expectedUser = createTestUser();
        expectedUser.setId(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.findById(userId);

        // Assert
        assertThat(result).isEqualTo(expectedUser);
        verify(userRepo).findById(userId);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void findById_nonExistingUser_shouldThrowException() {
        Long nonExistentId = 999L;
        when(userRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepo).findById(nonExistentId);
    }

    // ==================== ТЕСТЫ ДЛЯ findByUsername ====================
    @Test
    @DisplayName("Should return user when username exists")
    void findByUsername_existingUser_shouldReturnUser() {
        String username = "john";
        User expectedUser = createTestUser();
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        User result = userService.findByUsername(username);

        assertThat(result).isEqualTo(expectedUser);
        verify(userRepo).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw exception when username not found")
    void findByUsername_nonExistingUser_shouldThrowException() {
        String username = "nonexistent";
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername(username))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepo).findByUsername(username);
    }
}
*/

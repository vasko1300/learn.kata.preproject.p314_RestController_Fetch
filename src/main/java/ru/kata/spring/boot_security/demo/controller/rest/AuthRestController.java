package ru.kata.spring.boot_security.demo.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.domain.dto.LoginRequest;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.domain.dto.RoleDto;
import ru.kata.spring.boot_security.demo.domain.entity.User;
import ru.kata.spring.boot_security.demo.domain.mapper.ProfileMapper;
import ru.kata.spring.boot_security.demo.domain.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ProfileMapper profileMapper;
    private final RoleService roleService;

    public AuthRestController(AuthenticationManager authenticationManager, UserService userService, ProfileMapper profileMapper, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.profileMapper = profileMapper;
        this.roleService = roleService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Аутентифицируем пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
            // Устанавливаем аутентификацию в Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Получаем данные пользователя
            User user = userService.findByUsername(loginRequest.username());
            ProfileDto profileDto = profileMapper.toDto(user);
            // Возвращаем JSON с данными пользователя
            return ResponseEntity.ok(profileDto);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    /*@GetMapping("login-success")
    public ResponseEntity<UserInfo> getLoginSuccess(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (user.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.ok(userMapper.toDto(user));
        } else if (user.getRoles().contains("ROLE_USER")) {
            return ResponseEntity.ok(profileMapper.toDto(user));
        }
        return ResponseEntity.noContent().build();
    }*/

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,
                                                      HttpServletResponse response) {
        // Инвалидируем сессию
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        // Удаляем куку JSESSIONID
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("csrf")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return Map.of("csrfToken", csrfToken.getToken());
    }

    @GetMapping("roles")
    public ResponseEntity<List<RoleDto>> getCurrentUserRoles(Authentication authentication) {
        List<String> roleNames = userService.findProfileDtoByUsername(authentication.getName()).roles();
        return ResponseEntity.ok(roleNames.stream().map(roleService::findRoleDtoByName).toList());
    }
}
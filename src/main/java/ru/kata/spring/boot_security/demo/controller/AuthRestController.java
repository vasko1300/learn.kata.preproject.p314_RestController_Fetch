package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.domain.User;
import ru.kata.spring.boot_security.demo.dto.LoginRequest;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.mapper.ProfileMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    public AuthRestController(AuthenticationManager authenticationManager, UserService userService, ProfileMapper profileMapper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.profileMapper = profileMapper;
    }

    @PostMapping("login")
    public ResponseEntity<ProfileDto> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(loginRequest.username());

        return ResponseEntity.ok(profileMapper.toDto(user));
    }

    @GetMapping("csrf")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return Map.of("csrfToken", csrfToken.getToken());
    }
}

package ru.kata.spring.boot_security.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.domain.User;
import ru.kata.spring.boot_security.demo.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.mapper.ProfileMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private final ProfileMapper profileMapper;
    private final ObjectMapper objectMapper;

    public SuccessUserHandler(ProfileMapper profileMapper, ObjectMapper objectMapper) {
        this.profileMapper = profileMapper;
        this.objectMapper = objectMapper;
    }

    // Хендлер использует объект Authentication - пользователя авторизованной сессии, созданного как бин в классе-конфигураторе
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/api/users"); // JSON для админа
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/api/profile"); // JSON для пользователя
        } else {
            response.sendRedirect("/");
        }
    }
}
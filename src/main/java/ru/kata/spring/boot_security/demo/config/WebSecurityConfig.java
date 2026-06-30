package ru.kata.spring.boot_security.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
/* Начиная с Spring Security 5.7 (Spring Boot 2.7+ и 3.х) WebSecurityConfigurerAdapter (его требовалось расширять своим
 классом-конфигуратором и переопределять configure()) устарел и удален. Поэтому вместо configure() реализуем
 @Bean SecurityFilterChain и @Bean AuthenticationManager. @EnableWebSecurity по-прежнему нужен - подключает Spring
 Security в контекст, создает springSecurityFilterChain (главный фильтр), сканирует бины типа SecurityFilterChain (их
 теперь может быть несколько), настраивает базовую инфраструктуру*/
@EnableMethodSecurity
// Включает аннотации на методах контроллера: @PreAuthorize("hasRole('ADMIN')"), @PostAuthorize и т.д.
public class WebSecurityConfig {
    /*private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                    .antMatchers("/", "/api/auth/**", "/css/**", "/js/**").permitAll()
                    .antMatchers("/admin/**", "/api/users/**").hasRole("ADMIN")
                    .antMatchers("/user/**", "/api/profile/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    /*.defaultSuccessUrl("/api/auth/login-success", true)
                    .failureUrl("/api/auth/login-error")*/
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
//                    .successHandler(successUserHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
                    .and()
                /*.logout()
                    *//*.logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((
                                request,
                                response,
                                authentication
                        ) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Logged out successfully\"}");
                })*//*
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()*/
                .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
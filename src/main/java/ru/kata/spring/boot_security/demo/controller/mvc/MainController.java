package ru.kata.spring.boot_security.demo.controller.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileDto;

@Controller
public class MainController {
    private final String appName;

    public MainController(@Value("${spring.application.name}") String appName) {
        this.appName = appName;
    }

    @GetMapping({"/"})
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("appName", appName);
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";  // login.html (Thymeleaf)
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out");
            return "index";
        }
        model.addAttribute("profile", ProfileDto.empty());
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}

package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final String appName;

    public MainController(@Value("${spring.application.name}") String appName) {
        this.appName = appName;
    }

    @GetMapping({"/", "index"})
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("appName", appName);
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }
}

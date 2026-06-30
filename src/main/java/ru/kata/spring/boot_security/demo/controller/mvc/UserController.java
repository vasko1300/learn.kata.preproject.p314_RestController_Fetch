package ru.kata.spring.boot_security.demo.controller.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.domain.dto.ProfileDto;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model, Authentication auth) {
        model.addAttribute("profile", ProfileDto.empty());
        return "user/profile";
    }
}

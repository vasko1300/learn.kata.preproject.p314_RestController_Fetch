package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final String appName;
    private final UserService userService;
    private final RoleService roleService;

    public UserController(
            UserService userService,
            RoleService roleService,
            @Value("${spring.application.name}") String appName) {
        this.userService = userService;
        this.roleService = roleService;
        this.appName = appName;
    }

    @GetMapping({"/", "index"})
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("appName", appName);
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }
    @GetMapping({"user"})
    public String userPage(Model model, Authentication auth) {
        model.addAttribute("appName", appName);
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user/edit")
    public String showEditProfileForm(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/user/update")
    public String updateProfile(@ModelAttribute("user") User user,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user, "user");

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            return "redirect:/user/edit";
        }
    }

    @GetMapping({"admin"})
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    @GetMapping(value = "admin/new")
    public String showCreateForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }
    @GetMapping(value = "admin/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        User user = userService.getUserById(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "user-form";
    }
    @PostMapping(value = "admin/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                           Model model) {
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(id -> roleService.getRoleById(id))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        try {
            userService.saveUser(user, "admin");
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "user-form";
        }
    }
    @GetMapping(value = "admin/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
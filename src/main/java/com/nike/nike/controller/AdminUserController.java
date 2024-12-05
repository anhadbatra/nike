package com.nike.nike.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nike.nike.model.User;
import com.nike.nike.service.RoleService;
import com.nike.nike.service.UserService;
import com.nike.nike.validator.UserValidator;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    public AdminUserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = new UserValidator();
    }

    // List all users
    @GetMapping
    public String listUsers(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<User> users = (keyword != null && !keyword.isEmpty()) ?
                userService.searchUsers(keyword) :
                userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "admin/user-list";
    }

    // Show form to add a new user
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/user-form";
    }

    // Save new user
    @PostMapping
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                           Model model) {
        userValidator.validate(user, bindingResult);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/user-form";
        }
        // Encrypt the password
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // For new users, password should be mandatory
            bindingResult.rejectValue("password", "error.user", "Password is required");
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/user-form";
        }
        // Assign roles
        userService.assignRolesToUser(user, roleIds);
        return "redirect:/admin/users";
    }
    
    // Show form to edit a user
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/user-form";
    }

    // Update user
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                             Model model) {
        userValidator.validate(user, bindingResult);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/user-form";
        }
        User existingUser = userService.getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        // Update password if provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // Assign roles
        userService.assignRolesToUser(existingUser, roleIds);
        return "redirect:/admin/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}

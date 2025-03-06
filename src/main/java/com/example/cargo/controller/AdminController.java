package com.example.cargo.controller;

import com.example.cargo.annotation.RequireRole;
import com.example.cargo.model.Role;
import com.example.cargo.model.User;
import com.example.cargo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @RequireRole(Role.ADMIN)
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/manage_users";
    }

    @RequireRole(Role.ADMIN)
    @PostMapping("/users/updateRole")
    public String updateUserRole(@RequestParam Long userId, @RequestParam Role role) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
}


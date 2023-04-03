package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Вывод всех пользователей
    @GetMapping("/index")
    public String getUser(@AuthenticationPrincipal User authenticatedUser, Model model) {
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());

        return "index";
    }

    @PostMapping("/admin")
    public String addUser(@ModelAttribute("user") User newUser,
                          @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
        if (selectedRoles != null) {
            Set<Role> roles = new HashSet<>();
            for (String role : selectedRoles) {
                roles.add(roleService.getRoleByName(role));
            }
            newUser.setRoles(roles);
        }
        userService.save(newUser);
        return "redirect:/index";
    }

    @PatchMapping("/admin/{id}/edit")
    public String update(@ModelAttribute("editedUser") User editedUser,
                         @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
        if (selectedRoles != null) {
            Set<Role> roles = new HashSet<>();
            for (String elemArrSelectedRoles : selectedRoles) {
                roles.add(roleService.getRoleByName(elemArrSelectedRoles));
            }
            editedUser.setRoles(roles);
        }
        userService.update(editedUser);
        return "redirect:/index";
    }

    // Удаление юзера
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/index";
    }

}

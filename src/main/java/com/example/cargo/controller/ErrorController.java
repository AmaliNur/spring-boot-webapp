package com.example.cargo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", "У вас нет прав для выполнения этого действия");
        }
        return "error";
    }

    @RequestMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для выполнения этого действия");
        return "error";
    }
}

/**
 * Общие ошибки можно отображать с помощью #fields.errors().
 * Ошибки для полей отображаются с использованием #fields.hasErrors() или th:errors.
 * Серверная валидация обрабатывается через Spring Validator или Hibernate Validator с помощью
 * аннотаций (например @NotBlank).
 * Локализация сообщений легко интегрируется через MessageSource.
 */


package com.example.cargo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String accessDenied() {
        return "error/403"; // Указываем путь к файлу templates/error/403.html
    }
}

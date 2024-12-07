package com.example.cargo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AboutController {
    @GetMapping("/about")
    public String showAboutPage() {
        return "/about";
    }

    @GetMapping("/index")
    public String showMainPage() {
        return "/index";
    }
}

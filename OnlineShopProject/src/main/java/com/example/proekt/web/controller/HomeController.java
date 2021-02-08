package com.example.proekt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {
    //ZA da ja dava home i na locallhost:8080 bez/home
    @GetMapping
    public String HomePage() {
        return "redirect:/home";
    }

    //na /home ja vraka home
    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }
}

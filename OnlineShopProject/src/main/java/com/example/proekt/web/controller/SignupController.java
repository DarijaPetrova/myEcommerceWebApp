package com.example.proekt.web.controller;

import com.example.proekt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signup")
public class SignupController {
    @Autowired
    private AuthService authService;
    @GetMapping
    public String getSignUpPage(){
        return "signup";
    }
    @PostMapping
    public String signUpUser(@RequestParam String username, @RequestParam String password, @RequestParam String repeatedPassword){

        try{
            this.authService.signUpUser(username,password,repeatedPassword);
            return "redirect:/login?message=Uspesna registracija!";
        }
        catch (RuntimeException ex){
            return "redirect:/signup?error="+ex.getLocalizedMessage();
        }
    }
}

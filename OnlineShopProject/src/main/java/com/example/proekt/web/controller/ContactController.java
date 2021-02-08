package com.example.proekt.web.controller;

import com.example.proekt.model.Contact;
import com.example.proekt.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.mail.MessagingException;
import javax.validation.Valid;


@Controller
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private MailService mailService;
    @GetMapping
    public String getContactPage(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @PostMapping("/send")
    public String send(@Valid Contact contact, BindingResult bindingResult)  {

        if (bindingResult.hasErrors()) {

            System.out.println("validaciiski erori ima");
            return "contact";
        }

        try {

                String message = "<p>Име: " + contact.getName() + "</p>" + "<p>Телефон: " + contact.getPhone() + "</p>" + "<p>Email: " + contact.getEmail() + "</p>" + "<p>Порака:" + contact.getContent() + "</p>";
                mailService.send(contact.getEmail(), "petrov.venco@gmail.com", contact.getSubject(), message);

        }
        catch (MessagingException e){
            System.out.println("neuspesno");
            return "redirect:/contact?error=Vashata poraka ne e ispratena, obidete se povtorno.";
        }

        return "redirect:/contact?message=Vashata poraka e uspeshno ispratena.";



    }

}

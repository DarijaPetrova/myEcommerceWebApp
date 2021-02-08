package com.example.proekt.model;

import javax.validation.constraints.NotBlank;


public class Contact {
    private String name;
    private String phone;
    @NotBlank(message = "Полето е задолжително, внесете ја вашата емаил адреса.")
    private String email;
    private String address;
    @NotBlank(message = "Полето е задолжително, внесете наслов на пораката.")
    private String subject;
    @NotBlank(message = "Полето е задолжително, напишете ја вашата порака.")
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

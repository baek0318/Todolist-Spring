package com.peachberry.todolist.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3030", allowCredentials = "true")
public class AuthController {

    @PostMapping("/signin")
    public void signIn() {

    }

    @PostMapping("/signup")
    public void signup() {

    }

    @PostMapping("/signout")
    public void signOut() {

    }

    @PostMapping("/issueAccess")
    public void issueAccess() {

    }
}

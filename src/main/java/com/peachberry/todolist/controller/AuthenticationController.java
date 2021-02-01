package com.peachberry.todolist.controller;

import com.peachberry.todolist.dto.*;
import com.peachberry.todolist.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    ResponseEntity<?> memberSignUp(@Valid @RequestBody SignUpDTO signUpDTO) {

        SignUpSuccessDTO success = authenticationService.signup(signUpDTO);

        return ResponseEntity.ok(success);
    }

    @PostMapping("/signin")
    ResponseEntity<?> memberSignIn(@Valid @RequestBody SignInDTO signInDTO, HttpServletResponse response) {

        CookieDTO tokens = authenticationService.signin(signInDTO);

        response.addCookie(tokens.getAccessCookie());
        response.addCookie(tokens.getRefreshCookie());
        return ResponseEntity.ok(new SuccessResponseDTO("SignIn Success!!"));
    }


}

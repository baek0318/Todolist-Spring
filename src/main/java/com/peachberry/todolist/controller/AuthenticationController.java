package com.peachberry.todolist.controller;

import com.peachberry.todolist.dto.CookieDTO;
import com.peachberry.todolist.dto.request.SignInDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> memberSignUp(@Valid @RequestBody SignUpDTO user) {

        SignUpSuccessDTO response = authenticationService.signup(user);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void signUpFail(SignUpFailException ex) {
        logger.error(ex.getMessage());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> memberSignIn(@Valid @RequestBody SignInDTO user, HttpServletResponse response) {

        CookieDTO cookieDTO = authenticationService.signin(user);

        response.addCookie(cookieDTO.getAccessCookie());
        response.addCookie(cookieDTO.getRefreshCookie());
        return ResponseEntity.ok(SuccessResponseDTO.builder().response("SignIn Success").build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void signInFail(SignInFailException ex) {
        logger.error(ex.getMessage());
    }
}

package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.auth.CookieDTO;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import com.peachberry.todolist.controller.dto.auth.SignUpDTO;
import com.peachberry.todolist.controller.dto.auth.SignUpSuccessDTO;
import com.peachberry.todolist.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    private final CookieUtil cookieUtil;

    public AuthenticationController(AuthenticationService authenticationService, CookieUtil cookieUtil) {
        this.authenticationService = authenticationService;
        this.cookieUtil = cookieUtil;
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

    @GetMapping("/signout")
    public ResponseEntity<?> memberSignOut(HttpServletResponse response) {

        CookieDTO cookies = authenticationService.signout();

        response.addCookie(cookies.getAccessCookie());
        response.addCookie(cookies.getRefreshCookie());
        return ResponseEntity.ok(SuccessResponseDTO.builder().response("SignOut Success").build());
    }

    @GetMapping("/issueAccess")
    public ResponseEntity<?> issueAccess(HttpServletRequest request, HttpServletResponse response) {

        Cookie refreshCookie = cookieUtil.getRefreshCookie(request);

        Cookie cookie = authenticationService.issueAccess(refreshCookie);

        response.addCookie(cookie);
        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Issue Access Token Success").build());
    }
}

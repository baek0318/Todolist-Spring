package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.auth.*;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import com.peachberry.todolist.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

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
    public ResponseEntity<SignUpResponse> memberSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        Long response = authenticationService.signup(
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getName()
        );

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(AuthenticationController.class).memberSignUp(signUpRequest))
                .build();

        return ResponseEntity
                .created(uriComponents.encode().toUri())
                .body(new SignUpResponse(response));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void signUpFail(SignUpFailException ex) {
        logger.error(ex.getMessage());
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> memberSignIn(
            @Valid @RequestBody SignInRequest signInRequest,
            HttpServletResponse response
    ) {

        CookieDTO cookieDTO = authenticationService.signin(signInRequest.getEmail(), signInRequest.getPassword());

        response.addCookie(cookieDTO.getAccessCookie());
        response.addCookie(cookieDTO.getRefreshCookie());
        return ResponseEntity.ok(new SignInResponse(true));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void signInFail(SignInFailException ex) {
        logger.error(ex.getMessage());
    }

    @GetMapping("/signout")
    public ResponseEntity<SignOutResponse> memberSignOut(HttpServletResponse response) {

        CookieDTO cookies = authenticationService.signout();

        response.addCookie(cookies.getAccessCookie());
        response.addCookie(cookies.getRefreshCookie());
        return ResponseEntity.ok(new SignOutResponse(false));
    }

    @GetMapping("/issueAccess")
    public ResponseEntity<SuccessResponseDTO> issueAccess(HttpServletRequest request, HttpServletResponse response) {

        Cookie refreshCookie = cookieUtil.getRefreshCookie(request);

        Cookie cookie = authenticationService.issueAccess(refreshCookie);

        response.addCookie(cookie);
        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Issue Access Token Success").build());
    }
}

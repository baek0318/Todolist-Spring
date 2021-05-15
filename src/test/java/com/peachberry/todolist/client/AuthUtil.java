package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.controller.dto.auth.SignInRequest;
import com.peachberry.todolist.controller.dto.auth.SignUpRequest;
import com.peachberry.todolist.controller.dto.auth.SignUpResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AuthUtil {

    private final SignInRequest signInRequest = new SignInRequest("peachberry2@kakao.com", "1234");

    private final SignUpRequest signUpRequest = new SignUpRequest(
            "peachberry2@kakao.com",
            "peach",
            "1234");

    protected HttpHeaders headers;

    protected Long userId;

    protected Long signUp(TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest, headers);

        ResponseEntity<SignUpResponse> response = restTemplate
                .postForEntity(
                        "/auth/signup",
                        request,
                        SignUpResponse.class);

        return response.getBody().getId();
    }

    protected String signin(TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<SignInRequest> request = new HttpEntity<>(signInRequest, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/auth/signin", request, SuccessResponseDTO.class);
        return response.getHeaders().getValuesAsList(headers.SET_COOKIE).get(0);
    }

    abstract void setUp();
}

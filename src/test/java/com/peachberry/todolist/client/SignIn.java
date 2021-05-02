package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.controller.dto.auth.SignInRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public abstract class SignIn {

    private final SignInRequest signInRequest = new SignInRequest("peachberry2@kakao.com", "1234");

    protected HttpHeaders headers;

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

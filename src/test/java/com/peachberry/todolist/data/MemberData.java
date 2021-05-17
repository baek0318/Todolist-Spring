package com.peachberry.todolist.data;

import com.peachberry.todolist.controller.dto.auth.SignUpRequest;
import com.peachberry.todolist.controller.dto.auth.SignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberData {

    @Autowired
    private TestRestTemplate restTemplate;

    public Long saveMember(String email, String password, String name) {
        SignUpRequest signUpRequest = new SignUpRequest(email, name, password);
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest, headers);

        ResponseEntity<SignUpResponse> response = restTemplate
                .postForEntity("/auth/signup",request , SignUpResponse.class);

        return response.getBody().getId();
    }
}

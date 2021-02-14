package com.peachberry.todolist.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peachberry", "USER");

    @Test
    @DisplayName("회원가입 통합 테스트")
    void testSignUp() {

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignUpDTO> request = new HttpEntity<>(signUpDTO, headers);

        ResponseEntity<SignUpSuccessDTO> response = restTemplate
                .postForEntity("/api/auth/signup",request ,SignUpSuccessDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRole()).isEqualTo(Role.USER);
        assertThat(response.getBody().getName()).isEqualTo(signUpDTO.getName());
        assertThat(response.getBody().getEmail()).isEqualTo(signUpDTO.getEmail());
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 통합 테스트")
    void testSignUp_Failed() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignUpDTO> request = new HttpEntity<>(signUpDTO, headers);

        ResponseEntity<SignUpSuccessDTO> response = restTemplate
                .postForEntity()


    }

}

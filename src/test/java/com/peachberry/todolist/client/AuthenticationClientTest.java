package com.peachberry.todolist.client;

import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignInDTO;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.security.jwt.JwtUtil;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peachberry", "USER");

    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");

    private final Logger logger = LoggerFactory.getLogger(AuthenticationClientTest.class);

    @Test
    @Order(1)
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
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("로그인 통합 테스트")
    void testSignIn() {

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignInDTO> request = new HttpEntity<>(signInDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/auth/signin", request, SuccessResponseDTO.class);

        List<String> cookies = response.getHeaders().getValuesAsList(headers.SET_COOKIE);
        String access_header = cookies.get(0).split("=")[0]; //access_header
        String access_token = cookies.get(0).split(";")[0].split("=")[1]; //access_header
        String refresh_header = cookies.get(2).split("=")[0]; //refresh_token
        String refresh_token = cookies.get(2).split(";")[0].split("=")[1]; //refresh_token

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(access_header).isEqualTo("ACCESS-TOKEN");
        assertThat(refresh_header).isEqualTo("REFRESH-TOKEN");
        assertThat(jwtUtil.getEmailFromJwtToken(access_token)).isEqualTo(signInDTO.getEmail());
        assertThat(jwtUtil.getEmailFromJwtToken(refresh_token)).isEqualTo(signInDTO.getEmail());
    }

    @Test
    @DisplayName("로그아웃 통합 테스트")
    void testSignOut() {
        
    }
}

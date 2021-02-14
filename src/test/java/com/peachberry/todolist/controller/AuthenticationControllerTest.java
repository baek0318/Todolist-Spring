package com.peachberry.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.service.exception.SignUpFailException;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import com.peachberry.todolist.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(value = AuthenticationController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@Import({JwtAuthTokenFilter.class})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private CookieUtilImpl cookieUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;


    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peachberry", "USER");

    @Test
    @DisplayName("회원가입을 진행시에 올바른 응답값이 나오는지 확인")
    void testSignUp() throws Exception {
        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(any()))
                .willReturn(SignUpSuccessDTO.builder()
                        .email("peachberry@kakao.com")
                        .name("peachberry")
                        .role(Role.USER)
                        .id(1L)
                        .build());

        mockMvc.perform(post("/api/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("peachberry@kakao.com"))
                .andExpect(jsonPath("name").value("peachberry"))
                .andExpect(jsonPath("role").value("USER"))
                .andExpect(jsonPath("id").value(1L));
    }

    @Test
    @DisplayName("회원가입이 실패한 경우")
    void testSignUp_if_signup_fail() throws Exception {
        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(any())).willThrow(new SignUpFailException());

        mockMvc.perform(post("/api/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }
}

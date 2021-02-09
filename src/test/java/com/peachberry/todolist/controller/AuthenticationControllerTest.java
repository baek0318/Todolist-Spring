package com.peachberry.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.security.WebSecurityConfig;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import com.peachberry.todolist.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.times;
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


    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peach", "USER");

    @Test
    @DisplayName("회원가입을 진행시에 올바른 응답값이 나오는지 확인")
    @WithMockUser
    void signUpTest() throws Exception {

        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(signUpDTO))
                .willReturn(SignUpSuccessDTO.builder()
                        .email("peachberry@kakao.com")
                        .name("peach")
                        .id(1L)
                        .role(Role.USER).build());

        mockMvc.perform(post("/api/auth/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        verify(authenticationService, times(1)).signup(signUpDTO);
    }
}

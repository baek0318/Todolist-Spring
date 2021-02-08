package com.peachberry.todolist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.domain.UserDetailsImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import com.peachberry.todolist.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;


    private final SignUpDTO signUpDTO = new SignUpDTO("baek0318@icloud.com", "1234", "baek", "USER");


    @Test
    @DisplayName("로그인을 했을시 유효한 유저인지 검사")
    @WithMockUser
    void signUpTest() throws Exception {

        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(signUpDTO))
                .willReturn(SignUpSuccessDTO.builder()
                        .email("baek0318@icloud.com")
                        .name("baek")
                        .id(1L)
                        .role(Role.USER).build());

        mockMvc.perform(post("/api/auth/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("회원가입을 했을시 중복체크")
    void signUpDuplicateTest() throws Exception {

        String content = objectMapper.writeValueAsString(signUpDTO);

        mockMvc.perform(post("/api/auth/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

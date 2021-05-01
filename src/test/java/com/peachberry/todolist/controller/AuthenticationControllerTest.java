package com.peachberry.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.controller.dto.auth.CookieDTO;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.domain.UserDetailsImpl;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.controller.dto.auth.SignUpDTO;
import com.peachberry.todolist.controller.dto.auth.SignUpSuccessDTO;
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


import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(value = AuthenticationController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@Import({JwtAuthTokenFilter.class, CookieUtilImpl.class, JwtUtil.class, JwtAuthEntryPoint.class})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry4@kakao.com", "1234", "peachberry");

    private final SignInDTO signInDTO = new SignInDTO("peachberry4@kakao.com", "1234");

    @Test
    @DisplayName("회원가입을 진행시에 올바른 응답값이 나오는지 확인")
    void testSignUp() throws Exception {
        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(anyString(), anyString(), anyString()))
                .willReturn(SignUpSuccessDTO.builder()
                        .email("peachberry4@kakao.com")
                        .name("peachberry")
                        .role(Role.USER)
                        .id(1L)
                        .build());

        mockMvc.perform(post("/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("peachberry4@kakao.com"))
                .andExpect(jsonPath("name").value("peachberry"))
                .andExpect(jsonPath("role").value("USER"))
                .andExpect(jsonPath("id").value(1L));
    }

    @Test
    @DisplayName("회원가입이 실패한 경우")
    void testSignUp_if_signup_fail() throws Exception {
        String content = objectMapper.writeValueAsString(signUpDTO);

        given(authenticationService.signup(anyString(), anyString(), anyString()))
                .willThrow(new SignUpFailException("해당 이메일이 존재합니다"));

        mockMvc.perform(post("/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인이 성공한 경우")
    void testSignIn() throws Exception {

        String content = objectMapper.writeValueAsString(signInDTO);

        Cookie access = cookieUtil.createAccessCookie("");
        Cookie refresh = cookieUtil.createRefreshCookie("");

        given(authenticationService.signin(any()))
                .willReturn(new CookieDTO(access, refresh));

        mockMvc.perform(post("/auth/signin")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("response").value("SignIn Success"))
                .andExpect(status().isOk())
                .andExpect(cookie().httpOnly("ACCESS-TOKEN", true))
                .andExpect(cookie().httpOnly("REFRESH-TOKEN", true))
                .andExpect(cookie().exists("ACCESS-TOKEN"))
                .andExpect(cookie().exists("REFRESH-TOKEN"));

    }

    @Test
    @DisplayName("로그인이 실패한 경우")
    void testSignIn_Failed() throws Exception {
        String content = objectMapper.writeValueAsString(signInDTO);

        given(authenticationService.signin(any())).willThrow(new SignInFailException("로그인에 실패했습니다"));

        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃이 성공한 경우")
    void testSignOut_Success() throws Exception {

        Cookie access = cookieUtil.createLogoutAccessCookie();
        Cookie refresh = cookieUtil.createLogoutRefreshCookie();
        given(authenticationService.signout()).willReturn(new CookieDTO(access, refresh));

        mockMvc.perform(get("/auth/signout")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().httpOnly("ACCESS-TOKEN", true))
                .andExpect(cookie().httpOnly("REFRESH-TOKEN", true))
                .andExpect(cookie().exists("ACCESS-TOKEN"))
                .andExpect(cookie().exists("REFRESH-TOKEN"))
                .andExpect(cookie().maxAge("ACCESS-TOKEN", 0))
                .andExpect(cookie().maxAge("REFRESH-TOKEN", 0));
    }

    @Test
    @DisplayName("Access 토큰 재발급이 성공한 경우")
    void testIssueAccessToken_Success() throws Exception {
        String jws = jwtUtil.accessTokenGenerate("peachberry4@kakao.com");
        Cookie cookie = cookieUtil.createRefreshCookie(jws);

        given(userDetailsService.loadUserByUsername(any()))
                .willReturn(UserDetailsImpl.build(Member
                        .builder()
                        .email("peachberry4@kakao.com")
                        .password("1234")
                        .name("peachberry")
                        .authority(new Authority(Role.USER)).build()));

        given(authenticationService.issueAccess(any())).willReturn(cookieUtil.createAccessCookie("peachberry@kakao.com"));

        mockMvc.perform(get("/auth/issueAccess")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(cookie().httpOnly("ACCESS-TOKEN", true))
                .andExpect(cookie().exists("ACCESS-TOKEN"));
        verify(authenticationService, times(1)).issueAccess(any());
        verify(userDetailsService, times(1)).loadUserByUsername(any());
    }

    @Test
    @DisplayName("Access 토큰 재발급이 실패한 경우")
    void testIssueAccessToken_Fail() throws Exception {

        mockMvc.perform(get("/auth/issueAccess"))
                .andExpect(status().isUnauthorized());
    }
}

package com.peachberry.todolist.service;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthorityService authorityService;

    @Mock
    private MemberService memberService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peach", "USER");
    private final Authority authority = new Authority(Role.USER);
    private final Member member = Member.builder().email("peachberry@kakao.com").name("peach").authority(authority).password("1234").build();
    @Test
    @DisplayName("회원가입시에 올바른 응답인지 확인")
    void signUpValidate() {
        //given
        given(authorityService.saveAuthority()).willReturn(authority);
        given(memberService.join()).willReturn(1L);
        given(encoder.encode(member.getPassword())).willReturn("1234");

        //when
        SignUpSuccessDTO signUpSuccessDTO = authenticationService.signup(signUpDTO);

        //then

    }

    @Test
    @DisplayName("회원가입 시에 똑같은 이메일이 존재할 시에 오류발생")
    void signUpIfDuplicateEmailError() {
        //given

        //when
        //authenticationService.signup();

        //then
    }

    @Test
    @DisplayName("인증에 실패했을경우에 오류발생")
    void signInAuthenticationFail() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("인증에 성공했을 경우 올바른 인증 쿠키 발생했는지 확인")
    void signInAuthenticationSuccess() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("로그아웃시에 쿠키의 수명이 시간이 0인지 확인")
    void signOutCookieLifespanZero() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("리프레쉬 쿠키가 들어오면 새로운 액세스 쿠키 생성")
    void createNewAccessCookie() {
        //given

        //when

        //then
    }
}

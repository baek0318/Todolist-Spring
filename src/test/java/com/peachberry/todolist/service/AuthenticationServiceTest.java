package com.peachberry.todolist.service;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.service.exception.SignUpFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthorityService authorityService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder encoder;

    private JwtUtil jwtUtil = new JwtUtil();

    private CookieUtil cookieUtil = new CookieUtilImpl();

    @InjectMocks
    private AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);
    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peachberry", "USER");
    private final Authority authority = new Authority(Role.USER);
    private final Member member = Member.builder().email("peachberry@kakao.com").name("peachberry").authority(authority).password("1234").build();

    @Test
    @DisplayName("회원가입 부르기")
    void testSignUp() {
        given(memberRepository.findByEmail(any())).willReturn(Collections.emptyList());
        given(memberRepository.save(any())).willReturn(1L);
        given(authorityService.saveAuthority(any())).willReturn(authority);

        SignUpSuccessDTO success = authenticationService.signup(signUpDTO);

        Assertions.assertThat(success.getId()).isEqualTo(1L);
        Assertions.assertThat(success.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(success.getRole()).isEqualTo(member.getAuthority().getRole());
        Assertions.assertThat(success.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("회원가입 실패 오류 던지기")
    void testSignUp_Failed() {
        given(memberRepository.findByEmail(any())).willReturn(Collections.singletonList(member));

        Assertions.assertThatThrownBy(() -> authenticationService.signup(signUpDTO))
                .isInstanceOf(SignUpFailException.class);
    }
}

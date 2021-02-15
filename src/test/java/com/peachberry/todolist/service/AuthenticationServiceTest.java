package com.peachberry.todolist.service;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.CookieDTO;
import com.peachberry.todolist.dto.request.SignInDTO;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.Cookie;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private Authentication authentication;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);
    private final SignUpDTO signUpDTO = new SignUpDTO("peachberry@kakao.com", "1234", "peachberry", "USER");
    private final Authority authority = new Authority(Role.USER);
    private final Member member = Member.builder().email("peachberry@kakao.com").name("peachberry").authority(authority).password("1234").build();
    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");

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

    @Test
    @DisplayName("로그인 성공시키기")
    void testSignIn() {
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(jwtUtil.accessTokenGenerate(authentication)).willReturn("peachberry@kakao.com");
        given(jwtUtil.refreshTokenGenerate(authentication)).willReturn("peachberry@kakao.com");
        given(cookieUtil.createAccessCookie(anyString())).willReturn(new Cookie("ACCESS-TOKEN", "peachberry@kakao.com"));
        given(cookieUtil.createRefreshCookie(anyString())).willReturn(new Cookie("REFRESH-TOKEN", "peachberry@kakao.com"));

        CookieDTO cookies = authenticationService.signin(signInDTO);

        String access = cookies.getAccessCookie().getValue();
        String refresh = cookies.getRefreshCookie().getValue();

        //실제에서는 jwtUtil를 가지고 parsing을 진행해야한다
        Assertions.assertThat(access).isEqualTo(signInDTO.getEmail());
        Assertions.assertThat(refresh).isEqualTo(signInDTO.getEmail());
    }

    @Test
    @DisplayName("로그인 오류 던지기")
    void testSignInt_Failed() {
        given(authenticationManager.authenticate(any())).willThrow(new DisabledException("인증이 올바르지 않습니다"));

        Assertions.assertThatThrownBy(() -> authenticationService.signin(signInDTO)).isInstanceOf(SignInFailException.class);
    }

    @Test
    @DisplayName("로그아웃 쿠키 만들기")
    void testSignOut_Success() {
        Cookie refresh = new Cookie("REFRESH-TOKEN","logout");
        refresh.setHttpOnly(true);
        refresh.setPath("/api/auth/issueAccess");
        refresh.setMaxAge(0);

        Cookie access = new Cookie("ACCESS-TOKEN","logout");
        access.setHttpOnly(true);
        access.setPath("/");
        access.setMaxAge(0);

        given(cookieUtil.createLogoutAccessCookie()).willReturn(access);
        given(cookieUtil.createLogoutRefreshCookie()).willReturn(refresh);

        CookieDTO cookies = authenticationService.signout();

        Assertions.assertThat(cookies.getAccessCookie().getName()).isEqualTo("ACCESS-TOKEN");
        Assertions.assertThat(cookies.getRefreshCookie().getName()).isEqualTo("REFRESH-TOKEN");
        Assertions.assertThat(cookies.getAccessCookie().getMaxAge()).isEqualTo(0);
        Assertions.assertThat(cookies.getRefreshCookie().getMaxAge()).isEqualTo(0);
        Assertions.assertThat(cookies.getAccessCookie().isHttpOnly()).isTrue();
        Assertions.assertThat(cookies.getRefreshCookie().isHttpOnly()).isTrue();
    }
}

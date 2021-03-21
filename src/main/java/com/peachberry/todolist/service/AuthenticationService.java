package com.peachberry.todolist.service;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.controller.dto.auth.CookieDTO;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.controller.dto.auth.SignUpDTO;
import com.peachberry.todolist.controller.dto.auth.SignUpSuccessDTO;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.service.exception.SignInFailException;
import com.peachberry.todolist.service.exception.SignUpFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;


@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;

    private final AuthorityService authorityService;

    private final AuthenticationManager authenticationManager;

    private final CookieUtil cookieUtil;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder encoder;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(MemberRepository memberRepository, AuthorityService authorityService
            , AuthenticationManager authenticationManager, CookieUtil cookieUtil, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.authorityService = authorityService;
        this.authenticationManager = authenticationManager;
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @Transactional
    public SignUpSuccessDTO signup(SignUpDTO signUpDTO) {

        emailDuplication(signUpDTO.getEmail());

        Authority authority = authorityService.saveAuthority(Role.valueOf(signUpDTO.getAuthority()));

        Member member = Member.builder()
                .name(signUpDTO.getName())
                .email(signUpDTO.getEmail())
                .password(encoder.encode(signUpDTO.getPassword()))
                .authority(authority).build();

        Long id = memberRepository.save(member);

        return SignUpSuccessDTO.builder()
                .id(id)
                .role(member.getAuthority().getRole())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    private void emailDuplication(String email) {
        List<Member> member = memberRepository.findByEmail(email);
        if(!member.isEmpty()) {
            throw new SignUpFailException("동일한 이메일이 존재합니다");
        }
    }

    public CookieDTO signin(SignInDTO signInDTO) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));

            String access_token = jwtUtil.accessTokenGenerate(authentication);
            String refresh_token = jwtUtil.refreshTokenGenerate(authentication);

            Cookie access = cookieUtil.createAccessCookie(access_token);
            Cookie refresh = cookieUtil.createRefreshCookie(refresh_token);

            return new CookieDTO(access, refresh);
        }
        catch (Exception e) {
            logger.error(e.getMessage() + e.getClass());
            throw new SignInFailException("로그인에 실패했습니다");
        }
    }

    public CookieDTO signout() {

        Cookie access = cookieUtil.createLogoutAccessCookie();
        Cookie refresh = cookieUtil.createLogoutRefreshCookie();

        return new CookieDTO(access, refresh);
    }

    public Cookie issueAccess(Cookie refresh) {

        String refresh_jws = refresh.getValue();
        String email = jwtUtil.getEmailFromJwtToken(refresh_jws);
        String jws = jwtUtil.accessTokenGenerate(email);

        return cookieUtil.createAccessCookie(jws);
    }
}
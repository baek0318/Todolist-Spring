package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.CookieDTO;
import com.peachberry.todolist.dto.request.SignInDTO;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class AuthenticationService {

    private final AuthorityService authorityService;

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final String ACCESS = "ACCESS-TOKEN";
    private final String REFRESH = "REFRESH-TOKEN";

    public AuthenticationService(AuthorityService authorityService, MemberService memberService,
                                 AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.authorityService = authorityService;
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    public SignUpSuccessDTO signup(SignUpDTO user) {
        //멤버객체를 받는다
        //객체의 이메일과 동일한 이메일이 있는지 확인한다
        //같은 이메일이 있다면 오류 응답을 보내주면 된다
        //같은 이메일이 없다면 비밀번호를 암호화를 해서 Member객체를 생성한다
        //DB에 저장한다
        Authority authority = buildAuthority(checkAuthority(user.getAuthority()));
        Member member = Member.builder()
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .name(user.getName())
                .authority(authority)
                .build();
        memberService.join(member);

        return SignUpSuccessDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getAuthority().getRole())
                .id(member.getId())
                .build();
    }

    private Authority buildAuthority(Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    private Authority checkAuthority(String role) {
        if(role.equals("USER")) {
            return new Authority(Role.USER);
        }else {
            return new Authority(Role.ADMIN);
        }
    }

    public CookieDTO signin(SignInDTO member) {
        //이메일과 비밀번호를 입력받는다
        //인증절차를 만들어서 인증을 해주면 된다
        //SecurityContextHolder에 담는다
        //Access token과 Refresh token을 생성한다
        //------여기까지만 구현 나머지는 controller------
        //생성한 토큰을 쿠키에 담아서 응답을 보낸다
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtUtil.accessTokenGenerate(auth);
        String refreshToken = jwtUtil.refreshTokenGenerate(auth);

        Cookie accessCookie = cookieUtil.createAccessCookie(accessToken, ACCESS);
        Cookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken, REFRESH);

        return new CookieDTO(accessCookie, refreshCookie);
    }

    public CookieDTO signout() {
        //메서드가 실행되면
        //쿠키의 생명주기를 0으로 해서 만들어준다
        //CookieDTO에 담아서 반환한다
        Cookie accessCookie = cookieUtil.createLogoutAccessCookie(ACCESS);
        Cookie refreshCookie = cookieUtil.createLogoutRefreshCookie(REFRESH);

        return new CookieDTO(accessCookie, refreshCookie);
    }

    public Cookie issueToken(Cookie refreshCookie) {
        //메서드가 실행되면
        //만료가 되었기 때문에 이곳으로 요청이 온다
        //요청을 받으면 accessToken을 만든다
        //accessToken을 accessCookie에 담는다
        String token = refreshCookie.getValue();
        String email = jwtUtil.getEmailFromJwtToken(token);
        memberService.findByEmail(email);
        String jws = jwtUtil.accessTokenGenerate(email);

        return cookieUtil.createAccessCookie(jws, ACCESS);
    }
}
package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthorityService authorityService;

    private final MemberService memberService;

    public AuthenticationService(AuthorityService authorityService, MemberService memberService) {
        this.authorityService = authorityService;
        this.memberService = memberService;
    }

    public void signup(Member member) {
        //멤버객체를 받는다
        //객체의 이메일과 동일한 이메일이 있는지 확인한다
        //같은 이메일이 있다면 오류 응답을 보내주면 된다
        //같은 이메일이 없다면 비밀번호를 암호화를 해서 Member객체를 생성한다
        //DB에 저장한다
        //ok 응답을 보내준다
    }

    public void signin(String email, String password) {
        //이메일과 비밀번호를 입력받는다
        //인증절차를 만들어서 인증을 해주면 된다
        //SecurityContextHolder에 담는다
        //------여기까지만 구현 나머지는 controller------
        //Access token과 Refresh token을 생성한다
        //생성한 토큰을 쿠키에 담아서 응답을 보낸다
    }
}

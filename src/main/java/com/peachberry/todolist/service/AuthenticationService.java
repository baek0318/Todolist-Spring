package com.peachberry.todolist.service;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.dto.response.SignUpSuccessDTO;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.service.exception.SignUpFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AuthenticationService {

    private MemberRepository memberRepository;

    private AuthorityService authorityService;

    private PasswordEncoder encoder;

    public AuthenticationService(MemberRepository memberRepository, AuthorityService authorityService, PasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.authorityService = authorityService;
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
            throw new SignUpFailException();
        }
    }
}
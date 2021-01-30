package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.repository.AuthorityRepository;
import com.peachberry.todolist.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long join(Member member) {
        validateEmail(member.getEmail());
        return memberRepository.save(member);
    }

    private void validateEmail(String email) {
        List<Member> members = memberRepository.findByEmail(email);
        if(!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일 입니다");
        }
    }

    @Transactional
    public List<Member> findAll() {
        List<Member> members = memberRepository.findMembers();
        validateList(members);
        return members;
    }

    private void validateList(List<Member> members) {
        if(members.isEmpty()) {
            throw new IllegalStateException("회원이 존재하지 않습니다");
        }
    }

    @Transactional
    public Member findMember(Member member) {
        Member m = memberRepository.findById(member.getId());
        validateMember(m);
        return m;
    }

    private void validateMember(Member member) {
        if (member == null){
            throw new IllegalStateException("존재하지 않는 회원입니다");
        }
    }

}

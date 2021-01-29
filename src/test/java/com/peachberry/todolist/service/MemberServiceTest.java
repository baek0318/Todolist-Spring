package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원저장 중복확인")
    void testJoinMember() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member = new Member("baek0318@icloud.com", "1234", "baek", authority);
        given(memberRepository.save(member)).willReturn(1L);
        given(memberRepository.findByEmail(member.getEmail()))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(member));

        //when


        //then
    }

    @Test
    @DisplayName("전체 회원조회")
    void testFindAllMembers() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("단일 회원조회")
    void testFindMember() {
        //given

        //when

        //then

    }
}
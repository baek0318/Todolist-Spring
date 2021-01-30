package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @InjectMocks
    private MemberService memberService;

    private final Authority AUTHORITY = new Authority(Role.USER);
    private final Member MEMBER1 = new Member("baek0318@icloud.com", "1234", "baek", AUTHORITY);
    private final Member MEMBER2 = new Member("peachberry@gmail.com", "1234", "seung", AUTHORITY);

    @Test
    @DisplayName("회원저장 중복확인")
    void testJoinMember() {
        //given
        given(memberRepository.save(MEMBER1)).willReturn(1L);
        given(memberRepository.findByEmail(MEMBER1.getEmail()))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(MEMBER1));

        //when
        memberService.join(MEMBER1);

        //then
        verify(memberRepository, times(1)).save(MEMBER1);
        verify(memberRepository, times(1)).findByEmail(MEMBER1.getEmail());
        Assertions.assertThatThrownBy(() -> memberService.join(MEMBER1)).isInstanceOf(IllegalStateException.class);
        verify(memberRepository, times(2)).findByEmail(MEMBER1.getEmail());
    }


    //회원이 저장되어있는 저장소에 전체회원 조회를 시도한다
    //결과값이 비어있지 않다면 통과
    //결과값이 없다면 오류를 던진다
    @Test
    @DisplayName("전체 회원조회")
    void testFindAllMembers() {
        //given
        given(memberRepository.findMembers())
                .willReturn(Arrays.asList(MEMBER1, MEMBER2));

        //when
        List<Member> result = memberService.findAll();

        //then
        verify(memberRepository, times(1)).findMembers();
        Assertions.assertThat(result).isEqualTo(Arrays.asList(MEMBER1, MEMBER2));

    }

    @Test
    @DisplayName("전체 회원조회 아무도 존재하지 않음")
    void testEmptyFindAllMembers() {
        //given
        given(memberRepository.findMembers())
                .willReturn(Collections.emptyList());

        //when

        //then
        Assertions.assertThatThrownBy(() -> memberService.findAll()).isInstanceOf(IllegalStateException.class);
        verify(memberRepository, times(1)).findMembers();
    }

    @Test
    @DisplayName("단일 없는 회원조회")
    void testFindMember() {
        //given
        given(member.getId()).willReturn(1L);
        given(memberRepository.findById(1L))
                .willReturn(null);

        //when

        //then
        Assertions.assertThatThrownBy(() -> memberService.findMember(member)).isInstanceOf(IllegalStateException.class);
        verify(member, times(1)).getId();
        verify(memberRepository, times(1)).findById(1L);

    }
}
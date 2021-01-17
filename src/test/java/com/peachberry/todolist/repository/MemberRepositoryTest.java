package com.peachberry.todolist.repository;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback
    //제대로 저장이 되는지 확인
    void saveAndLoadMemberData() {
        //given
        /*
        String email = "baek0318@icloud.com";
        String password = "1234";
        String name = "baek";
        Authority authority = new Authority();
        Member member = new Member();

        //when
        memberRepository.save();*/

        //then
    }
}

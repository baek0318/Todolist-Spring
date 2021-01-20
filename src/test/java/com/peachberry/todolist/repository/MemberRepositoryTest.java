package com.peachberry.todolist.repository;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback
    @DisplayName("제대로 저장 검색되는지 확인")
    void saveAndLoadMemberData() {
        //given

        //when

        //then
    }
}

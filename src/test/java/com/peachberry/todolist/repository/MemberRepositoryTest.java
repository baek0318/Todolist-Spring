package com.peachberry.todolist.repository;


import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OrderBy;
import javax.validation.*;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    private final String EMAIL = "baek0318@icloud.com";
    private final String PASSWORD = "12345678910";
    private final String NAME = "PEACHBERRY";
    private final Authority authority = new Authority(Role.USER);

    private Validator validator;

    @BeforeEach
    void saveAuthority() {
        authorityRepository.save(authority);
    }

    @BeforeEach
    void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("제대로 저장 검색되는지 확인")
    void saveAndLoadMemberData() {
        //given
        Member member = new Member(EMAIL, PASSWORD, NAME, authority);

        //when
        Long id = memberRepository.save(member);
        List<Member> result = memberRepository.findByEmail(EMAIL);

        //then
        Assertions.assertThat(result.get(0).getId()).isEqualTo(id);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    //올바르지 않은 이메일 형식을 입력하였을때
    @Test
    @DisplayName("이메일 형식이 올바르지 않습니다")
    void checkEmailValidation() {
        //given
        String wrong = "baek0318@.com";
        Member member = new Member(wrong ,PASSWORD,NAME,authority);

        //when
        Set<ConstraintViolation<Member>> validates = validator.validate(member);

        //then
        Assertions.assertThat(validates.iterator().next().getInvalidValue()).isEqualTo(wrong);
    }

    @Test
    @DisplayName("id로 member 찾기")
    void testFindById() {
        //given
        Member member = new Member(EMAIL, PASSWORD, NAME, authority);

        //when
        Long id = memberRepository.save(member);
        Member result = memberRepository.findById(id);

        //then
        Assertions.assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("전체 멤버 불러오기")
    void checkFindAllMember() {
        //given
        Member member1 = new Member(EMAIL, PASSWORD, NAME, authority);
        Member member2 = new Member("peachberry0318@gmail.comm", "1234", "baek", authority);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findMembers();

        //then
        Assertions.assertThat(member1).isEqualTo(result.get(0));
        Assertions.assertThat(member2).isEqualTo(result.get(1));
    }
}

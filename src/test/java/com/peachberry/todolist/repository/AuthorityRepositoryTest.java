package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    @DisplayName("값을 저장후 저장 확인")
    void authoritySaveNFind() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);
        //when
        Authority result = authorityRepository.findByRole(Role.USER);

        //then
        Assertions.assertThat(result).isEqualTo(authority);
    }

    @Test
    @DisplayName("없는 권한을 찾을 때")
    void authorityNoName() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);

        //when
        Authority resultNull = authorityRepository.findByRole(Role.ADMIN);
        Authority resultNotNull = authorityRepository.findByRole(Role.USER);
        //then
        Assertions.assertThat(resultNull).isEqualTo(null);
        Assertions.assertThat(resultNotNull).isEqualTo(authority);
    }

    @Test
    @DisplayName("권한정보 저장후 해당 정보가 존재하는지 확인")
    void authorityExist() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);

        //when
        Boolean resultTrue = authorityRepository.existByRole(Role.USER);

        //then
        Assertions.assertThat(resultTrue).isEqualTo(true);
    }

    @Test
    @DisplayName("없는 권한이 존재하는지 확인")
    void authorityNotExist() {
        //given
        Authority authority = new Authority(Role.USER);

        //when
        Boolean result = authorityRepository.existByRole(Role.ADMIN);

        //then
        Assertions.assertThat(result).isEqualTo(false);
    }
}

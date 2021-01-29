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

import java.util.List;


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
        List<Authority> result = authorityRepository.findByRole(Role.USER);

        //then
        Assertions.assertThat(result.get(0).getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("없는 권한을 찾을 때")
    void authorityNoName() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);

        //when
        List<Authority> resultEmpty = authorityRepository.findByRole(Role.ADMIN);
        List<Authority> result = authorityRepository.findByRole(Role.USER);
        //then
        Assertions.assertThat(resultEmpty.isEmpty()).isTrue();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getRole()).isEqualTo(Role.USER);
    }

}

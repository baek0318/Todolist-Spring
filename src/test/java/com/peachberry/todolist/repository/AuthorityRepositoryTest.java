package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import(AuthorityRepository.class)
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
        Optional<Authority> result = authorityRepository.findByRole(Role.USER);

        //then
        Assertions.assertThat(result.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("없는 권한을 찾을 때")
    void authorityNoName() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);

        //when
        Optional<Authority> resultEmpty = authorityRepository.findByRole(Role.ADMIN);
        Optional<Authority> result = authorityRepository.findByRole(Role.USER);

        //then
        Assertions.assertThat(resultEmpty.isEmpty()).isTrue();
        Assertions.assertThat(result.get().getRole()).isEqualTo(Role.USER);
    }

}

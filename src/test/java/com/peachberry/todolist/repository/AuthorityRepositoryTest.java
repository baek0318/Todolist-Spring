package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test @Rollback
    void AuthoritySaveAndFind() {
        //given
        Authority authority = new Authority(Role.USER);

        //when
        authorityRepository.save(authority);
        Authority auth = authorityRepository.findByRole(Role.USER);

        //then
        Assertions.assertThat(auth).isEqualTo(authority);
    }


}

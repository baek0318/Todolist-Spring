package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test @Rollback
    @DisplayName("값을 저장하고 꺼내서 확인")
    void authoritySaveAndFind() {
        //given
        Authority authority = new Authority(Role.USER);
        //when
        authorityRepository.save(authority);
        Authority auth = authorityRepository.findByRole(Role.USER);
        //then
        Assertions.assertThat(auth).isEqualTo(authority);
    }

    @Test @Rollback
    @DisplayName("authority 저장시에 아무런 인자도 넘어오지 않았을 때")
    void authorityNoArgument() {
        //given
        Authority authority = new Authority();

        //when
        Set<ConstraintViolation<Authority>> validates = validator.validate(authority);

        //then
        Assertions.assertThat(validates.iterator().next().getMessage()).isEqualTo("널이어서는 안됩니다");
    }

    @Test @Rollback
    @DisplayName("없는 권한을 찾을 때")
    void authorityNoName() {
        //given
        Authority authority = new Authority(Role.USER);
        authorityRepository.save(authority);
        //when
        Authority result = authorityRepository.findByRole(Role.ADMIN);
        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test @Rollback
    @DisplayName("권한정보 저장후 해당 정보가 존재하는지 확인")
    void authorityExist() {
        //given
        Authority authority = new Authority(Role.USER);
        //when
        authorityRepository.save(authority);
        Boolean result = authorityRepository.existByRole(Role.USER);
        //then
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test @Rollback
    @DisplayName("없는 권한이 존재하는지 확인")
    void authorityNotExist() {
        //given
        Authority authority = new Authority(Role.USER);
        //when
        authorityRepository.save(authority);
        Boolean result = authorityRepository.existByRole(Role.ADMIN);
        //then
        Assertions.assertThat(result).isEqualTo(false);
    }
}

package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorityRepositoryTest {

    @Mock
    private AuthorityRepository authorityRepository;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    @DisplayName("값을 Email로 찾아 확인")
    void authoritySaveAndFind() {
        //given
        given(authorityRepository.findByRole(Role.USER)).willReturn(new Authority(1L, Role.USER));
        //when
        Authority result = authorityRepository.findByRole(Role.USER);
        //then
        verify(authorityRepository, times(1)).findByRole(Role.USER);
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getRole()).isEqualTo(Role.USER);
    }

    @Test
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

    @Test
    @DisplayName("권한정보 저장후 해당 정보가 존재하는지 확인")
    void authorityExist() {
        //given
        given(authorityRepository.existByRole(Role.USER)).willReturn(true);
        //when

        Boolean result = authorityRepository.existByRole(Role.USER);
        //then
        verify(authorityRepository, times(1)).existByRole(Role.USER);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
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

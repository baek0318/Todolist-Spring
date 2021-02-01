package com.peachberry.todolist.service;

import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.SignInDTO;
import com.peachberry.todolist.dto.SignUpDTO;
import com.peachberry.todolist.dto.SignUpSuccessDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Mock
    private SignInDTO signInDTO;

    @Mock
    private SignUpDTO signUpDTO;

    @Test
    @DisplayName("로그인 프로세스 테스트")
    void signInTest() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("회원가입 프로세스 테스트")
    void signUpTest() {
        //given
        given(signUpDTO.getEmail()).willReturn("baek0318@icloud.com");
        given(signUpDTO.getPassword()).willReturn("1234");
        given(signUpDTO.getName()).willReturn("baek");
        given(signUpDTO.getAuthority()).willReturn("USER");

        //when
        SignUpSuccessDTO success = authenticationService.signup(signUpDTO);

        //then
        Assertions.assertThat(success.getAuthority().getRole()).isEqualTo(Role.USER);
        Assertions.assertThat(success.getEmail()).isEqualTo(signUpDTO.getEmail());
        Assertions.assertThat(success.getName()).isEqualTo(signUpDTO.getName());
    }



}

package com.peachberry.todolist.controller;

import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.dto.request.SignUpDTO;
import com.peachberry.todolist.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AuthenticationControllerTest {

    @Mock
    private SignUpDTO signUpDTO;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEachTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    @DisplayName("회원가입 api")
    void signUpApiTest() throws Exception {
        //given
        //given(authenticationService.signup()).willReturn()

        //when
        //authenticationController.memberSignUp();

        //then
        mockMvc.perform(post("/api/auth/signup"));
    }
}

package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.AuthorityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityService authorityService;

    //권한이 들어왔을때 존재하는 권한이라면 오류를 던지고
    //권한이 존재하지 않는다면 저장하면 된다
    @Test
    @DisplayName("권한 저장")
    void testSaveAuthority() {
        //given
        Authority authority = new Authority(Role.USER);
        given(authorityRepository.save(authority))
                .willReturn(1L);
        given(authorityRepository.findByRole(authority.getRole()))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(authority));

        //when
        authorityService.saveAuthority(authority);

        //then
        verify(authorityRepository, times(1)).save(authority);
        assertThatThrownBy(()-> authorityService.saveAuthority(authority)).isInstanceOf(IllegalStateException.class);
    }

}
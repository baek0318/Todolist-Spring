package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.AuthorityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

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
        given(authorityRepository.findById(1L))
                .willReturn(authority);
        given(authorityRepository.findByRole(authority.getRole()))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(authority));

        //when
        Authority result = authorityService.saveAuthority(authority);
        Authority result2 = authorityService.saveAuthority(authority);

        //then
        verify(authorityRepository, times(1)).save(authority);
        verify(authorityRepository, times(1)).findById(1L);
        verify(authorityRepository, times(2)).findByRole(authority.getRole());
        Assertions.assertThat(result).isEqualTo(authority);
        Assertions.assertThat(result).isEqualTo(result2);
    }

}
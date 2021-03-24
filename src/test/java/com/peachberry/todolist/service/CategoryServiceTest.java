package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.service.dto.CategoryServiceDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CategoryService categoryService;

    private final Authority authority = new Authority(Role.USER);
    private final Member member = new Member("baek0318@icloud.com", "1234", "baek", authority);
    private final Category category = new Category("하루일과", member);

    @Test
    @DisplayName("카테고리 저장하기 중복확인")
    void saveTest() {
        //given
        given(memberRepository.findById(1L))
                .willReturn(member);
        given(categoryRepository.findByTitle(category.getTitle(), 1L))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(category));

        //when
        categoryService.saveCategory(new CategoryServiceDto.Save(1L, category.getTitle()));

        //then
        verify(memberRepository, times(1))
                .findById(1L);
        verify(categoryRepository, times(1))
                .findByTitle(category.getTitle(), 1L);
        Assertions.assertThatThrownBy(() -> categoryService.saveCategory(new CategoryServiceDto.Save(1L, category.getTitle()))).isInstanceOf(IllegalStateException.class);
        verify(memberRepository, times(2))
                .findById(1L);
        verify(categoryRepository, times(2))
                .findByTitle(category.getTitle(), 1L);
    }

    @Test
    @DisplayName("카테고리 주제가 존재하지 않을떄")
    void findByTitleTest() {
        //given
        given(categoryRepository.findByTitle("하루일과", 1L))
                .willReturn(Collections.singletonList(category))
                .willReturn(Collections.emptyList());

        //when
        Category result = categoryService.findByTitle(new CategoryServiceDto.FindByTitle(1L, category.getTitle()));

        //then
        Assertions.assertThat(result).isEqualTo(category);
        Assertions.assertThatThrownBy(() -> categoryService.findByTitle(new CategoryServiceDto.FindByTitle(1L, category.getTitle()))).isInstanceOf(IllegalStateException.class);
        verify(categoryRepository, times(2)).findByTitle("하루일과", 1L);
    }
}

package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.dto.CategoryDTO;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
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
    private final CategoryDTO categoryDTO = CategoryDTO.builder().category(category).member(member).build();

    @Test
    @DisplayName("카테고리 저장하기 중복확인")
    void saveTest() {
        //given
        given(memberRepository.findById(categoryDTO.getMember().getId()))
                .willReturn(member);
        given(categoryRepository.findByTitle(categoryDTO.getCategory().getTitle(), categoryDTO.getMember()))
                .willReturn(Collections.emptyList())
                .willReturn(Collections.singletonList(category));

        //when
        categoryService.saveCategory(categoryDTO);

        //then
        verify(memberRepository, times(1))
                .findById(categoryDTO.getMember().getId());
        verify(categoryRepository, times(1))
                .findByTitle(categoryDTO.getCategory().getTitle(), categoryDTO.getMember());
        Assertions.assertThatThrownBy(() -> categoryService.saveCategory(categoryDTO)).isInstanceOf(IllegalStateException.class);
        verify(memberRepository, times(2))
                .findById(categoryDTO.getMember().getId());
        verify(categoryRepository, times(2))
                .findByTitle(categoryDTO.getCategory().getTitle(), categoryDTO.getMember());
    }

    @Test
    @DisplayName("카테고리 주제가 존재하지 않을떄")
    void findByTitleTest() {
        //given
        given(categoryRepository.findByTitle("하루일과", member))
                .willReturn(Collections.singletonList(category))
                .willReturn(Collections.emptyList());

        //when
        Category result = categoryService.findByTitle(categoryDTO);

        //then
        Assertions.assertThat(result).isEqualTo(category);
        Assertions.assertThatThrownBy(() -> categoryService.findByTitle(categoryDTO)).isInstanceOf(IllegalStateException.class);
        verify(categoryRepository, times(2)).findByTitle("하루일과", member);
    }
}

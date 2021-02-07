package com.peachberry.todolist.repository;

import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    @DisplayName("저장후 확인")
    void testSaveAndFindById() {
        //given
        Category category = new Category("하루일과", null);

        //when
        Long id = categoryRepository.save(category);
        Category result = categoryRepository.findById(id);

        //then
        Assertions.assertThat(result.getTitle()).isEqualTo("하루일과");
        Assertions.assertThat(result.getId()).isEqualTo(id);

    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void testFindAll() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Category category1 = new Category("하루일과", member1);
        Category category2 = new Category("하루공부", member1);
        Category category3 = new Category("하루운동", member1);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        Long id1 = categoryRepository.save(category1);
        Long id2 = categoryRepository.save(category2);
        Long id3 = categoryRepository.save(category3);

        List<Category> result = categoryRepository.findAll(member1.getId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 확인")
    void testFindByTitle() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Category category1 = new Category("하루일과", member1);
        Category category2 = new Category("기본", member2);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Long id = categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> result = categoryRepository.findByTitle("하루일과", member1.getId());

        //then
        Assertions.assertThat(result.isEmpty()).isFalse();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(id);
        Assertions.assertThat(result.get(0).getTitle()).isEqualTo("하루일과");
    }

    @Test
    @DisplayName("카테고리 이름 변경")
    void testReviseCategory() {
        //given
        Category category = new Category("하루일과", null);

        //when
        Long id = categoryRepository.save(category);
        Long id2 = categoryRepository.reviseCategory("교회일과", id);
        Category result = categoryRepository.findById(id);

        //then
        Assertions.assertThat(id).isEqualTo(id2);
        Assertions.assertThat(result.getTitle()).isEqualTo("교회일과");
        Assertions.assertThat(result.getId()).isEqualTo(id2);
    }

    @Test
    @DisplayName("카테고리 아이디로 삭제")
    void testDeleteById() {
        //given
        Category category = new Category("하루일과", null);
        Category category2 = new Category("기본", null);

        //when
        Long id = categoryRepository.save(category);
        categoryRepository.deleteById(id);
        Long id2 = categoryRepository.save(category2);

        Category result = categoryRepository.findById(id);
        Category result2 = categoryRepository.findById(id2);

        //then
        Assertions.assertThat(result).isNull();
        Assertions.assertThat(result2.getId()).isEqualTo(id2);
    }

}
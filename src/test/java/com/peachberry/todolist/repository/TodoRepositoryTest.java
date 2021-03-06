package com.peachberry.todolist.repository;

import com.peachberry.todolist.QueryDslConfig;
import com.peachberry.todolist.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Import({TodoRepository.class,
        MemberRepository.class,
        AuthorityRepository.class,
        CategoryRepository.class,
        TodoRepositorySupport.class,
        QueryDslConfig.class})
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TodoRepositorySupport todoRepositorySupport;

    @Test
    @DisplayName("저장이 제대로 되는지 확인")
    void testSave() {
        //given
        String title = "밥 먹기";
        TodoStatus status = TodoStatus.ING;
        Todo todo = new Todo(null,null, LocalDate.now(), title, status);

        //when
        Long id = todoRepository.save(todo);
        Todo result = todoRepository.findById(id);

        //then
        Assertions.assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("모든 데이터 불러오기")
    void testFindAll() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Todo todo1 = new Todo(member1, null, LocalDate.now(), "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, LocalDate.now(), "화장실 가기", TodoStatus.COMPLETE);
        Todo todo3 = new Todo(member2, null, LocalDate.now(), "학교가기", TodoStatus.ING);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> result = todoRepository.findAll(member1.getId());

        //then
        System.out.println(result);
        Assertions.assertThat(result).isEqualTo(Arrays.asList(todo1));
    }

    @Test
    @DisplayName("완료상태로 Todo 찾기")
    void findByStatus() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Todo todo1 = new Todo(member1, null, LocalDate.now(), "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member1, null, LocalDate.now(), "화장실 가기", TodoStatus.COMPLETE);
        Todo todo3 = new Todo(member2, null, LocalDate.now(), "학교가기", TodoStatus.ING);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> complete = todoRepositorySupport.findDynamicQuery(null, "COMPLETE", member1.getId());
        List<Todo> ing = todoRepositorySupport.findDynamicQuery(null, "ING", member2.getId());

        //then
        Assertions.assertThat(complete).isEqualTo(Arrays.asList(todo1, todo2));
        Assertions.assertThat(complete).isNotEqualTo(Arrays.asList(todo3));
        Assertions.assertThat(ing).isEqualTo(Arrays.asList(todo3));
        Assertions.assertThat(ing).isNotEqualTo(Arrays.asList(todo1, todo2));
    }



    @Test
    @DisplayName("Todo 삭제하기")
    void deleteById() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Todo todo1 = new Todo(member1, null, LocalDate.now(), "밥 먹기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        Long id = todoRepository.save(todo1);

        todoRepository.deleteById(id);
        List<Todo> result2 = todoRepositorySupport.findDynamicQuery(null, "COMPLETE", member1.getId());
        Todo result3 = todoRepository.findById(id);

        //then
        Assertions.assertThat(result2.size()).isEqualTo(0);
        Assertions.assertThat(result3).isNull();
    }

    @Test
    @DisplayName("카테고리로 Todo 검색하기")
    void  testFindByCategory() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Category category = new Category("하루일과", member1);
        Todo todo1 = new Todo(member1, category, LocalDate.now(), "밥 먹기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        categoryRepository.save(category);
        Long id = todoRepository.save(todo1);

        List<Todo> result = todoRepository.findByCategory(category.getId(), member1.getId());

        //then
        Assertions.assertThat(result).isEqualTo(Arrays.asList(todo1));
    }

    @Test
    @DisplayName("todo update 하기")
    void testUpdateTodo() {
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Category category = new Category("하루일과", member1);
        Todo todo1 = new Todo(member1, category, LocalDate.now(), "밥 먹기", TodoStatus.COMPLETE);
        Todo todo = new Todo(member1, category, LocalDate.now(), "학교가기", TodoStatus.ING);

        authorityRepository.save(authority);
        memberRepository.save(member1);
        categoryRepository.save(category);
        Long id = todoRepository.save(todo1);
        todo.setId(id);

        Long result = todoRepository.update(todo);

        Assertions.assertThat(result).isEqualTo(id);
    }
}
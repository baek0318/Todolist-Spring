package com.peachberry.todolist.repository;

import com.peachberry.todolist.AppConfig;
import com.peachberry.todolist.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("저장이 제대로 되는지 확인")
    void testSave() {
        //given
        Calendar calendar = new Calendar(2021, 1, 26);
        String title = "밥 먹기";
        TodoStatus status = TodoStatus.ING;
        Todo todo = new Todo(null,null,calendar, title, status);

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
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.ING);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);
        Todo todo3 = new Todo(member1, null, calendar, "학교가기", TodoStatus.ING);

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
        Assertions.assertThat(result).isEqualTo(Arrays.asList(todo1, todo3));
    }

    @Test
    @DisplayName("완료상태로 Todo 찾기")
    void findByStatus() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);
        Todo todo3 = new Todo(member1, null, calendar, "학교가기", TodoStatus.ING);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> complete = todoRepository.findByStatus(TodoStatus.COMPLETE, member1.getId());
        List<Todo> ing = todoRepository.findByStatus(TodoStatus.ING, member1.getId());

        //then
        Assertions.assertThat(complete).isEqualTo(Arrays.asList(todo1));
        Assertions.assertThat(complete).isNotEqualTo(Arrays.asList(todo3));
        Assertions.assertThat(ing).isEqualTo(Arrays.asList(todo3));
        Assertions.assertThat(ing).isNotEqualTo(Arrays.asList(todo1));
    }

    @Test
    @DisplayName("날짜로 Todo 찾기")
    void findByCalendar() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Calendar calendar2 = new Calendar(2021, 1, 27);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);
        Todo todo3 = new Todo(member1, null, calendar2, "학교가기", TodoStatus.ING);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> result = todoRepository.findByCalendar(calendar2, member1.getId());

        //then
        Assertions.assertThat(result).isEqualTo(Arrays.asList(todo3));
        Assertions.assertThat(result).isNotEqualTo(Arrays.asList(todo1));
    }

    @Test
    @DisplayName("완료 상태 변경하기")
    void reviseStatus() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Long id1 = todoRepository.save(todo1);
        todoRepository.save(todo2);

        todoRepository.reviseStatus(TodoStatus.ING, id1);
        List<Todo> yes = todoRepository.findByStatus(TodoStatus.ING, member1.getId());

        //then
        System.out.println(yes.get(0).getStatus());
        Assertions.assertThat(yes.get(0).getStatus()).isEqualTo(TodoStatus.ING);

    }

    @Test
    @DisplayName("Todo 수정하기")
    void reviseTodo() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Long id1 = todoRepository.save(todo1);
        todoRepository.save(todo2);

        todoRepository.reviseTodo("운동하기", id1);
        List<Todo> yes = todoRepository.findAll(member1.getId());

        //then
        Assertions.assertThat(yes.get(0).getTitle()).isEqualTo("운동하기");
        Assertions.assertThat(yes.get(0).getTitle()).isNotEqualTo("밥 먹기");
    }

    @Test
    @DisplayName("Todo 날짜 수정하기")
    void reviseCalendar() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Member member2 = new Member("peachberry@icloud.com","1234","seung", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);
        Todo todo2 = new Todo(member2, null, calendar, "화장실 가기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Long id1 = todoRepository.save(todo1);
        todoRepository.save(todo2);

        Calendar c = new Calendar(2021, 1, 31);
        todoRepository.reviseCalendar(c, id1);
        List<Todo> yes = todoRepository.findByCalendar(c, member1.getId());

        //then
        Assertions.assertThat(yes.get(0).getCalendar()).isEqualTo(c);
        Assertions.assertThat(yes.get(0).getCalendar()).isNotEqualTo(calendar);
    }

    @Test
    @DisplayName("Todo 삭제하기")
    void deleteById() {
        //given
        Authority authority = new Authority(Role.USER);
        Member member1 = new Member("baek0318@icloud.com","1234","baek", authority);
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, null, calendar, "밥 먹기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        Long id = todoRepository.save(todo1);

        todoRepository.deleteById(id);
        List<Todo> result2 = todoRepository.findByStatus(TodoStatus.COMPLETE, member1.getId());
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
        Calendar calendar = new Calendar(2021, 1, 26);
        Todo todo1 = new Todo(member1, category, calendar, "밥 먹기", TodoStatus.COMPLETE);

        //when
        authorityRepository.save(authority);
        memberRepository.save(member1);
        categoryRepository.save(category);
        Long id = todoRepository.save(todo1);

        List<Todo> result = todoRepository.findByCategory(category.getId(), member1.getId());

        //then
        Assertions.assertThat(result).isEqualTo(Arrays.asList(todo1));
    }
}
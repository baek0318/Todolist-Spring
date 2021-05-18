package com.peachberry.todolist.data;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@Component
public class TodoData {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long saveTodo(Long userId, Long categoryId, LocalDate date, String title, TodoStatus status) {
        Member member = em.find(Member.class, userId);
        Category category = em.find(Category.class, categoryId);
        Todo todo = Todo.builder()
                .member(member)
                .category(category)
                .date(date)
                .title(title)
                .status(status)
                .build();

        em.persist(todo);

        return todo.getId();
    }
}

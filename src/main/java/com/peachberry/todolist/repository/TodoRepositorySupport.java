package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.peachberry.todolist.domain.QTodo.todo;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TodoRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public TodoRepositorySupport(JPAQueryFactory queryFactory) {
        super(Todo.class);
        this.queryFactory = queryFactory;
    }

    public List<Todo> findDynamicQuery(String date, String status, Long memberId) {
        return queryFactory
                .selectFrom(todo)
                .where(
                        getStatus(status),
                        getDate(date),
                        todo.member.id.eq(memberId)
                )
                .fetch();
    }

    private BooleanExpression getDate(String date) {
        if(date == null) {
            return null;
        }
        return todo.date.eq(LocalDate.parse(date));
    }

    private BooleanExpression getStatus(String status) {
        if(status == null) {
            return null;
        }
        return todo.status.eq(TodoStatus.valueOf(status));
    }
}

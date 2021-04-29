package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.*;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.repository.TodoRepository;
import com.peachberry.todolist.repository.TodoRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    private final TodoRepositorySupport todoRepositorySupport;

    private final MemberRepository memberRepository;

    private final CategoryRepository categoryRepository;

    public TodoService(TodoRepository todoRepository,
                       TodoRepositorySupport todoRepositorySupport,
                       MemberRepository memberRepository,
                       CategoryRepository categoryRepository
    ) {
        this.todoRepository = todoRepository;
        this.todoRepositorySupport = todoRepositorySupport;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long saveTodo(Todo todo, Long memberId, Long categoryId) {
        if(categoryId != null) {
            Category category = categoryRepository.findById(categoryId);
            todo.setCategory(category);
        }
        Member member = memberRepository.findById(memberId);
        todo.setMember(member);

        return todoRepository.save(todo);
    }

    @Transactional
    public List<Todo> findAllTodo(Long member_id) {
        return todoRepository.findAll(member_id);
    }

    @Transactional
    public Todo findTodoById(Long todo_id) {
        return todoRepository.findById(todo_id);
    }

    @Transactional
    public List<Todo> findTodoByCategory(Long category_id, Long member_id) {
        return todoRepository.findByCategory(category_id, member_id);
    }

    @Transactional
    public Long updateTodo(Todo todo, Long categoryId) {
        Todo found = todoRepository.findById(todo.getId());
        Category category = categoryRepository.findById(categoryId);

        return todoRepository.update(changeProperty(todo, found, category));
    }

    private Todo changeProperty(Todo todo, Todo found, Category category) {
        if(todo.getTitle() != null) {
            found.changeTitle(todo.getTitle());
        }
        if(todo.getDate() != null) {
            found.changeDate(todo.getDate());
        }
        if(todo.getStatus() != null) {
            found.changeStatus(todo.getStatus());
        }
        found.setCategory(category);

        return found;
    }

    @Transactional
    public void deleteTodo(Long todo_id) {
        todoRepository.deleteById(todo_id);
    }

    @Transactional
    public List<Todo> findByDynamicParam(String status, String datetime, Long memberId) {

        return todoRepositorySupport.findDynamicQuery(datetime, status, memberId);
    }
}

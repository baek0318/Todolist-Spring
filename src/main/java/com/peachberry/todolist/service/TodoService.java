package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.*;
import com.peachberry.todolist.controller.dto.todo.TodoDTO;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    private final MemberRepository memberRepository;

    private final CategoryRepository categoryRepository;

    public TodoService(TodoRepository todoRepository, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        this.todoRepository = todoRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long saveTodo(TodoDTO todoDTO) {
        List<Category> category = categoryRepository.findByTitle(todoDTO.getCategory(), todoDTO.getMember_id());
        Member member = memberRepository.findById(todoDTO.getMember_id());
        Todo todo = Todo.builder()
                .calendar(todoDTO.getCalendar())
                .category(category.get(0))
                .title(todoDTO.getTitle())
                .member(member)
                .status(TodoStatus.ING)
                .build();
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
    public List<Todo> findTodoByStatus(TodoStatus status, Long member_id) {
        return todoRepository.findByStatus(status, member_id);
    }

    @Transactional
    public List<Todo> findTodoByCalendar(Calendar calendar, Long member_id) {
        return todoRepository.findByCalendar(calendar, member_id);
    }

    @Transactional
    public List<Todo> findTodoByCategory(Long category_id, Long member_id) {
        return todoRepository.findByCategory(category_id, member_id);
    }

    @Transactional
    public void reviseTodoByStatus(TodoStatus status, Long todo_id) {
        todoRepository.reviseStatus(status, todo_id);
    }

    @Transactional
    public void reviseTodoByTitle(String change_title, Long todo_id) {
        todoRepository.reviseTodo(change_title, todo_id);
    }

    @Transactional
    public void reviseTodoByCalendar(Calendar calendar, Long todo_id) {
        todoRepository.reviseCalendar(calendar, todo_id);
    }

    @Transactional
    public void deleteTodo(Long todo_id) {
        todoRepository.deleteById(todo_id);
    }
}

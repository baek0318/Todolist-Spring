package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.*;
import com.peachberry.todolist.dto.request.TodoDTO;
import com.peachberry.todolist.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void saveTodo(TodoDTO todoDTO) {


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

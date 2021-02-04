package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final Logger logger = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public List<Todo> findAllTodo(Member member) {
        return todoRepository.findAll(member);
    }

    public Todo findTodoById(Todo todo) {
        return todoRepository.findById(todo.getId());
    }

    public List<Todo> findTodoByStatus(Todo todo, Member member) {
        return todoRepository.findByStatus(todo.getStatus(), member);
    }

    public List<Todo> findTodoByCalendar(Todo todo, Member member) {
        return todoRepository.findByCalendar(todo.getCalendar(), member);
    }

    public List<Todo> findTodoByCategory(Todo todo, Member member) {
        return todoRepository.findByCategory(todo.getCategory(), member);
    }

    public void reviseTodoByStatus(Todo todo) {
        todoRepository.reviseStatus(todo.getStatus(), todo.getId());
    }

    public void reviseTodoByTitle(Todo todo) {
        todoRepository.reviseTodo(todo.getTitle(), todo.getId());
    }

    public void reviseTodoByCalendar(Todo todo) {
        todoRepository.reviseCalendar(todo.getCalendar(), todo.getId());
    }

    public void deleteTodo(Todo todo) {
        todoRepository.deleteById(todo.getId());
    }
}

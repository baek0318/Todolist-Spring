package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TodoResponse {

    @Data
    @NoArgsConstructor
    public static class Save {
        private Long id;

        public Save(Long id) {
            this.id = id;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Update {
        private Long id;

        public Update(Long id) {
            this.id = id;
        }
    }

    @Data
    @NoArgsConstructor
    public static class TodoInfo {

        private Long id;

        private CategoryResponse.CategoryInfo category;

        private LocalDate date;

        private String title;

        private TodoStatus status;

        @Builder
        public TodoInfo(Long id, CategoryResponse.CategoryInfo category, LocalDate date, String title, TodoStatus status) {
            this.id = id;
            this.category = category;
            this.date = date;
            this.title = title;
            this.status = status;
        }

        public TodoInfo(Todo todo) {
            this.id = todo.getId();
            this.category = new CategoryResponse.CategoryInfo(todo.getCategory());
            this.date = todo.getDate();
            this.title = todo.getTitle();
            this.status = todo.getStatus();
        }

    }

    @Data
    @NoArgsConstructor
    public static class TodoInfoList {

        private List<TodoInfo> todoInfoList;

        public TodoInfoList(List<TodoInfo> todoInfoList) {
            this.todoInfoList = todoInfoList;
        }
    }
}

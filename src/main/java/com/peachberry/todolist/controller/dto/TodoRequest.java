package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodoRequest {

    @Data
    @NoArgsConstructor
    public static class Save {

        private String title;

        private LocalDate date;

        private Long categoryId;

        public Save(String title, LocalDate date, Long categoryId) {
            this.title = title;
            this.date = date;
            this.categoryId = categoryId;
        }

        public Todo toEntity() {
            return new Todo(
                    null,
                    null,
                    this.date,
                    this.title,
                    TodoStatus.ING
            );
        }
    }

    @Data
    @NoArgsConstructor
    public static class Update {

        private Long id;

        private CategoryControllerDto.CategoryInfo category;

        private String title;

        private String date;

        private String status;

        public Update(Long id, CategoryControllerDto.CategoryInfo category, String title, String date, String status) {
            this.id = id;
            this.category = category;
            this.title = title;
            this.date = date;
            this.status = status;
        }

        public Todo toEntity() {
            Todo todo = new Todo(
                    null,
                    null,
                    LocalDate.parse(
                            this.date,
                            DateTimeFormatter.ISO_LOCAL_DATE),
                    this.title,
                    TodoStatus.valueOf(this.status)
            );
            todo.setId(this.id);
            return todo;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Delete {

        private Long id;

        public Delete(Long id) {
            this.id = id;
        }
    }
}

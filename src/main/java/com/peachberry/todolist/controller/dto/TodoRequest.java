package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}

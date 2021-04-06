package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TodoRequest {

    @Data
    @NoArgsConstructor
    public static class Save {

        private String title;

        private LocalDateTime dateTime;

        private Long categoryId;

        public Save(String title, LocalDateTime dateTime, Long categoryId) {
            this.title = title;
            this.dateTime = dateTime;
            this.categoryId = categoryId;
        }

        public Todo toEntity() {
            return new Todo(
                    null,
                    null,
                    this.dateTime,
                    this.title,
                    TodoStatus.ING
            );
        }
    }
}

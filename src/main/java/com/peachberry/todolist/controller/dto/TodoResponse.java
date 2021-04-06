package com.peachberry.todolist.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class TodoResponse {

    @Data
    @NoArgsConstructor
    public static class Save {
        private Long id;

        public Save(Long id) {
            this.id = id;
        }
    }
}

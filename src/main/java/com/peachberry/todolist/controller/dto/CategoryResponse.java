package com.peachberry.todolist.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class CategoryResponse {

    @Data
    @NoArgsConstructor
    public static class Update {
        Long id;

        public Update(Long id) {
            this.id = id;
        }
    }
}

package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Category;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryResponse {

    @Data
    @NoArgsConstructor
    public static class Update {
        Long id;

        public Update(Long id) {
            this.id = id;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Save {
        Long id;

        public Save(Long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryInfo {

        private Long id;
        private String title;

        public CategoryInfo(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        public CategoryInfo(Category category) {
            this.id = category.getId();
            this.title = category.getTitle();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryList {

        private List<CategoryInfo> categoryList;

        public CategoryList(List<CategoryInfo> categoryList) {
            this.categoryList = categoryList;
        }
    }
}

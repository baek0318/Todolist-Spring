package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.service.dto.CategoryServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryControllerDto {

    @Getter
    public static class Delete {
        private Long id;

        public Delete(Long id) {
            this.id = id;
        }

        protected Delete() {}
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
    }

    @Getter
    public static class CategoryList {

        private List<Category> categoryList;

        public CategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        protected CategoryList() {}
    }

    @Getter
    public static class Save {

        private String title;

        @Builder
        public Save(String title) {
            this.title = title;
        }

        protected Save() {}

        public CategoryServiceDto.Save toServiceDto(Long memberId) {
            return new CategoryServiceDto.Save(memberId, this.title);
        }
    }

    @Getter
    public static class Update {

        private Long id;

        private String changedTitle;

        public Update(Long id, String changedTitle) {
            this.id = id;
            this.changedTitle = changedTitle;
        }

        protected Update() {}

        public CategoryServiceDto.UpdateTitle toServiceDto() {
            return new CategoryServiceDto.UpdateTitle(id, changedTitle);
        }
    }
}

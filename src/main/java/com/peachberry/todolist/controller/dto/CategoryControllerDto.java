package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.service.dto.CategoryServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class CategoryControllerDto {

    @Getter
    @NoArgsConstructor
    public static class Delete {
        private Long id;

        public Delete(Long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Save {

        @NotBlank
        private String title;

        @Builder
        public Save(String title) {
            this.title = title;
        }

        public CategoryServiceDto.Save toServiceDto(Long memberId) {
            return new CategoryServiceDto.Save(memberId, this.title);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Update {

        private Long id;

        private String changedTitle;

        public Update(Long id, String changedTitle) {
            this.id = id;
            this.changedTitle = changedTitle;
        }

        public CategoryServiceDto.UpdateTitle toServiceDto() {
            return new CategoryServiceDto.UpdateTitle(id, changedTitle);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryInfo {

        private Long id;

        public CategoryInfo(Long id) {
            this.id = id;
        }
    }
}

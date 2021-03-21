package com.peachberry.todolist.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryServiceDto {

    @Getter
    @NoArgsConstructor
    public static class Save {

        private Long memberId;

        private String title;

        public Save(Long memberId, String title) {
            this.memberId = memberId;
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FindByTitle {

        private Long memberId;

        private String title;

        public FindByTitle(Long memberId, String title) {
            this.memberId = memberId;
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateTitle {

        private Long id;

        private String title;

        public UpdateTitle(Long id, String title) {
            this.id = id;
            this.title = title;
        }
    }
}

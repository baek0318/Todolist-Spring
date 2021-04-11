package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Authority;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityResponse {

    @Data
    @NoArgsConstructor
    public static class AuthorityInfo {

        private String authName;

        public AuthorityInfo(Authority authority) {
            this.authName = authority.getRole().toString();
        }
    }
}

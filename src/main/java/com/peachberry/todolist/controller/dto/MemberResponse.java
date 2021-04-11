package com.peachberry.todolist.controller.dto;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Data
    @NoArgsConstructor
    public static class MemberInfo {
        private Long id;

        private String email;

        private String name;

        private AuthorityResponse.AuthorityInfo authorityInfo;

        public MemberInfo(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.authorityInfo = new AuthorityResponse.AuthorityInfo(member.getAuthority());
        }
    }
}

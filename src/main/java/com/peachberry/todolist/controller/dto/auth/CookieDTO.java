package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CookieDTO {

    private Cookie accessCookie;

    private Cookie refreshCookie;

    public CookieDTO(Cookie accessCookie, Cookie refreshCookie) {
        this.accessCookie = accessCookie;
        this.refreshCookie = refreshCookie;
    }
}

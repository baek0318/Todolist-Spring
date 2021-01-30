package com.peachberry.todolist.dto;

import lombok.Getter;

import javax.servlet.http.Cookie;

@Getter
public class CookieDTO {

    private final Cookie accessCookie;

    private final Cookie refreshCookie;

    public CookieDTO(Cookie accessCookie, Cookie refreshCookie) {
        this.accessCookie = accessCookie;
        this.refreshCookie = refreshCookie;
    }
}

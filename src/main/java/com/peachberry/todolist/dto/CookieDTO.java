package com.peachberry.todolist.dto;

import javax.servlet.http.Cookie;

public class CookieDTO {

    private final Cookie accessCookie;

    private final Cookie refreshCookie;

    public CookieDTO(Cookie accessCookie, Cookie refreshCookie) {
        this.accessCookie = accessCookie;
        this.refreshCookie = refreshCookie;
    }
}

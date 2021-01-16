package com.peachberry.todolist.security.cookie;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class CookieUtilImpl implements CookieUtil{

    private final int MAX_AGE = 3600000*24*14;

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";

    private final String REFRESH_TOKEN = "REFRESH-TOKEN";

    @Override
    public Cookie createAccessCookie(String token, String name) {
        Cookie cookie = new Cookie(name,token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        return cookie;
    }

    @Override
    public Cookie createRefreshCookie(String token, String name) {
        Cookie cookie = new Cookie(name,token);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/issueAccess");
        cookie.setMaxAge(MAX_AGE);
        return cookie;
    }

    @Override
    public Cookie createLogoutRefreshCookie(String name) {
        Cookie cookie = new Cookie(name,"logout");
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/issueAccess");
        cookie.setMaxAge(0);
        return cookie;
    }

    @Override
    public Cookie createLogoutAccessCookie(String name) {
        Cookie cookie = new Cookie(name,"logout");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    @Override
    public Cookie getAccessCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return cookies != null ? getCookie(cookies, ACCESS_TOKEN) : null;
    }

    @Override
    public Cookie getRefreshCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return cookies != null ? getCookie(cookies, REFRESH_TOKEN) : null;
    }

    private Cookie getCookie(Cookie[] cookies, String type) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(type))
                .findFirst()
                .orElse(null);
    }
}

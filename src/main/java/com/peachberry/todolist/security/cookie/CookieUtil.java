package com.peachberry.todolist.security.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface CookieUtil {

    public Cookie createAccessCookie(String token, String name);

    public Cookie createRefreshCookie(String token, String name);

    public Cookie createLogoutRefreshCookie(String name);

    public Cookie createLogoutAccessCookie(String name);

    public Cookie getAccessCookie(HttpServletRequest request);

    public Cookie getRefreshCookie(HttpServletRequest request);
}

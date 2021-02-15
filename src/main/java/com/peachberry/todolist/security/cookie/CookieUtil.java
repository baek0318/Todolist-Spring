package com.peachberry.todolist.security.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface CookieUtil {

    public Cookie createAccessCookie(String token);

    public Cookie createRefreshCookie(String token);

    public Cookie createLogoutRefreshCookie();

    public Cookie createLogoutAccessCookie();

    public Cookie getAccessCookie(HttpServletRequest request);

    public Cookie getRefreshCookie(HttpServletRequest request);
}

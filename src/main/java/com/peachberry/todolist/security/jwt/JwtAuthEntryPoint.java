package com.peachberry.todolist.security.jwt;

import com.peachberry.todolist.security.cookie.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Autowired
    public JwtAuthEntryPoint(JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {

        String jwt = parseAccessJwt(request);
        if(jwt != null && jwtUtil.checkAccessTokenExpire(jwt)){
            logger.info("Token expire info");
            response.setHeader("Expire" ,"true");
            response.setStatus(202);
        }
        else {
            logger.error("Unauthorized error: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        }
    }

    private String parseAccessJwt(HttpServletRequest request) {
        Cookie cookieAuth = cookieUtil.getAccessCookie(request);
        return cookieAuth != null ? cookieAuth.getValue() : null;
    }
}

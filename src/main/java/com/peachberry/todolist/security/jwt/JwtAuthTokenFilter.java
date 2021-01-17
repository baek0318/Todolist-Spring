package com.peachberry.todolist.security.jwt;

import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthTokenFilter(JwtUtil jwtUtil, CookieUtil cookieUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.userDetailsService = userDetailsService;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //ACCESS-TOKEN validate part
        try {
            String jwt = parseAccessJwt(request);
            checkJwtValidate(request, jwt);
        }catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        //REFRESH-TOKEN validate part
        try {
            String jwt = parseRefreshJwt(request);
            checkJwtValidate(request, jwt);
        }catch (UsernameNotFoundException e) {
            logger.error("Cannot find user about refresh token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void checkJwtValidate(HttpServletRequest request, String jwt)  {
        if(jwt != null && jwtUtil.validateJwtToken(jwt)) {
            //jwt 토큰 파싱
            String email = jwtUtil.getEmailFromJwtToken(jwt);

            //인증을 위해서 userdetailsService에서 userDetails를 가져온다
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            //위에서 loadUserByEamil를 통해 가져온 userDetail로 검증이 끝났으니 SecurityContextHolder에 넣기 위해서 토큰을 만든다
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            //authentication에 request를 통해서 디테일을 추가해준다
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //인증되었다는 의미로 SecurityContextHolder에 추가해준다
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String parseAccessJwt(HttpServletRequest request) {
        Cookie cookieAuth = cookieUtil.getAccessCookie(request);
        if(cookieAuth != null)
            return cookieAuth.getValue();
        return null;
    }

    private String parseRefreshJwt(HttpServletRequest request) {
        Cookie cookieAuth = cookieUtil.getRefreshCookie(request);
        if(cookieAuth != null)
            return cookieAuth.getValue();
        return null;
    }
}

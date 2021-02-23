package com.peachberry.todolist.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = TodoController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@Import({JwtAuthTokenFilter.class, CookieUtilImpl.class, JwtUtil.class, JwtAuthEntryPoint.class})
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private CookieUtil cookieUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Test
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {

    }

    @Test
    @DisplayName("카테고리 저장시 중복 에러")
    void testSaveCategory_DuplicateError() {

    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {

    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {

    }

    @Test
    @DisplayName("해당 카테고리 업데이트하기")
    void testUpdateCategory() {

    }

    @Test
    @DisplayName("해당 카테고리 삭제하기")
    void testDeleteCategory() {

    }
}

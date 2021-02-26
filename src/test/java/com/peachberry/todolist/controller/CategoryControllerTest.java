package com.peachberry.todolist.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.dto.CategoryDTO;
import com.peachberry.todolist.dto.CategoryUpdateDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import com.peachberry.todolist.service.CategoryService;
import com.peachberry.todolist.service.exception.CategorySaveFail;
import com.peachberry.todolist.service.exception.CategoryUpdateFail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
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
    @MockBean
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(CategoryControllerTest.class);

    @Test
    @DisplayName("카테고리 저장 실패했을 경우")
    @WithMockUser
    void testSaveCategory() throws Exception {

        given(categoryService.saveCategory(anyLong(), any())).willThrow(new IllegalStateException("저장에 실패했습니다"));

        CategoryDTO categoryDTO = new CategoryDTO("하루일과");
        String content = objectMapper.writeValueAsString(categoryDTO);
        mockMvc.perform(post("/api/{id}/category/save", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() throws Exception {

        given(categoryService.findAll(anyLong())).willReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/api/{id}/category/search/all", 1))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() throws Exception {

        given(categoryService.findByTitle(anyString(), anyLong())).willReturn(new Category("하루일과", null));

        mockMvc.perform(get("/api/{id}/category/search", 1 )
                .param("title", "하루일과"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 업데이트하기")
    void testUpdateCategory() throws Exception {

        given(categoryService.reviseTitle(anyString(), anyLong())).willReturn(1L);

        String content = objectMapper.writeValueAsString(new CategoryUpdateDTO(1L, "하루종일"));

        mockMvc.perform(post("/api/{id}/category/update", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Update Complete"))
                .andDo(print());

        verify(categoryService, times(1)).reviseTitle(anyString(), anyLong());
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 업데이트 실패")
    void testUpdateCategory_Failed() throws Exception {

        given(categoryService.reviseTitle(anyString(), anyLong())).willThrow(new CategoryUpdateFail("Update Fail"));

        String content = objectMapper.writeValueAsString(new CategoryUpdateDTO(1L, "하루종일"));

        mockMvc.perform(post("/api/{id}/category/update", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}

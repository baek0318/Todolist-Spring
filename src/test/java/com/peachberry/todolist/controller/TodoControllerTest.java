package com.peachberry.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import com.peachberry.todolist.dto.TodoListDTO;
import com.peachberry.todolist.dto.request.TodoDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.cookie.CookieUtilImpl;
import com.peachberry.todolist.security.jwt.JwtAuthEntryPoint;
import com.peachberry.todolist.security.jwt.JwtAuthTokenFilter;
import com.peachberry.todolist.security.jwt.JwtUtil;
import com.peachberry.todolist.security.service.UserDetailsServiceImpl;
import com.peachberry.todolist.service.TodoService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TodoController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@Import({JwtAuthTokenFilter.class, CookieUtilImpl.class, JwtUtil.class, JwtAuthEntryPoint.class})
public class TodoControllerTest {

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
    private TodoService todoService;

    private final Todo todo = Todo.builder()
            .title("밥 먹기")
            .category(new Category("매일하루", null))
            .calendar(new Calendar(2021, 2, 18))
            .member(null)
            .status(TodoStatus.ING).build();

    private final Logger logger = LoggerFactory.getLogger(TodoControllerTest.class);
    //진행해야할 테스트
    /*
        1. 전체 Todo목록 가져오기
        2. Todo목록이 비었을 경우에 어떻게 할지 정하기
        3. 특정 카테고리로 Todo목록을 가져오기
        4. 특정 날짜로 Todo목록을 가져오기
        5. 특정 상태로 Todo목록을 가져오기
        6. 특정 Todo상태 변경하기
        7. 특정 Todo제목 변경하기
        8. 특정 Todo캘린더 변경하기
        9. 특정 Todo를 삭제하기 테스트?
        위의 테스트의 문제점 하위 컴포넌트에서 이미 검증된 것들임
        굳이 진행할 필요가 있을까??

     */

    @Test
    @DisplayName("전체 Todo 목록 가져오기")
    @WithMockUser
    void testGetTotalTodo() throws Exception {

        given(todoService.findAllTodo(anyLong()))
                .willReturn(Collections.singletonList(todo));

        MvcResult result = mockMvc.perform(get("/api/{id}/todo/search/all", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();


        TodoListDTO todoListDTO = objectMapper.readValue(result.getResponse().getContentAsString(), TodoListDTO.class);
        logger.info(result.getResponse().getContentAsString()+"\n");
        Assertions.assertThat(todoListDTO.getTodoList().get(0).getTitle()).isEqualTo("밥 먹기");
        Assertions.assertThat(todoListDTO.getTodoList().get(0).getCalendar().getYear()).isEqualTo(2021);
        Assertions.assertThat(todoListDTO.getTodoList().get(0).getCalendar().getDay()).isEqualTo(18);
        Assertions.assertThat(todoListDTO.getTodoList().get(0).getCalendar().getMonth()).isEqualTo(2);
        verify(todoService, times(1)).findAllTodo(anyLong());
    }

    @Test
    @DisplayName("Todo 가져오기")
    @WithMockUser
    void testGetTodo() {

    }
}

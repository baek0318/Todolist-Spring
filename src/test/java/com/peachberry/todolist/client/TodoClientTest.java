package com.peachberry.todolist.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.controller.dto.TodoResponse;
import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.controller.dto.todo.TodoListDTO;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.controller.dto.todo.TodoDTO;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(TodoClientTest.class);

    private final SignInDTO signInDTO = new SignInDTO("peachberry2@kakao.com", "1234");

    private HttpHeaders headers;

    private final TodoDTO todoDTO = TodoDTO.builder()
            .title("공부하기")
            .calendar(new Calendar(2021, 2, 23))
            .category("하루일과")
            .member_id(1L)
            .build();

    private String signin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<SignInDTO> request = new HttpEntity<>(signInDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/auth/signin", request, SuccessResponseDTO.class);
        return response.getHeaders().getValuesAsList(HttpHeaders.SET_COOKIE).get(0);
    }

    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());
        this.headers = headers;
    }

    @Test
    @DisplayName("Todo 저장하기")
    void testSaveTodo() {
        TodoRequest.Save saveDto = new TodoRequest.Save("코딩하기", LocalDateTime.now(), null);
        HttpEntity<TodoRequest.Save> request = new HttpEntity<>(saveDto, headers);

        ResponseEntity<TodoResponse.Save> responseEntity = restTemplate
                .postForEntity("/todo/{member-id}",
                        request,
                        TodoResponse.Save.class,
                        1);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("전체 Todo 목록 가져오기")
    void testLoadAllData() {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfoList> response = restTemplate
                .exchange("/todo/{member-id}/all",
                        HttpMethod.GET,
                        request,
                        TodoResponse.TodoInfoList.class,
                        1);

        TodoResponse.TodoInfoList result = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }


    @Test
    @DisplayName("특정 Todo 하나만 가져오기")
    void testGetOneTodo() {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfo> responseEntity = restTemplate.exchange(
                "/todo/{member-id}?title=하루일과",
                HttpMethod.GET,
                request,
                TodoResponse.TodoInfo.class,
                1L);

        TodoResponse.TodoInfo response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getTitle()).isEqualTo("하루일과");

    }

    @Test
    @DisplayName("일치하는 상태 Todo 가져오기")
    void testGetStatusTodo() {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfoList> responseEntity = restTemplate.exchange(
                "/todo/{member-id}?status=ING",
                HttpMethod.GET,
                request,
                TodoResponse.TodoInfoList.class,
                1L);

        TodoResponse.TodoInfoList response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response).isNotNull();
    }
/*
    @Test
    @DisplayName("일치하는 날짜 Todo 가져오기")
    void testGetCalendarTodo() {

    }

    @Test
    @DisplayName("일치하는 카테고리 Todo 가져오기")
    void testGetCategoryTodo() {

    }

 */
}

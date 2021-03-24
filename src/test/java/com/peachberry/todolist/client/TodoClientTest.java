package com.peachberry.todolist.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(TodoClientTest.class);

    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");

    //Todo 전체 Todo목록 가져오기 (load) (완료)
    //Todo 특정 카테고리 목록 가져오기
    //Todo 선택한 Todo정보 가져오기 (add)
    //Todo 작성 / 초기에 등록할 때 카테고리 지정 (1개만 가능) / 초기에 등록할 때 날짜 지정 (년 월 일) (NotNull)
    //Todo 수정 (update)
    //Todo 완료 (update)
    //Todo 삭제 (delete)
    //Todo 날짜로 검색 (search)
    //Todo 완료로 검색 (search)
    //Todo 카테고리로 검색 (search)

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

        HttpEntity request = new HttpEntity(signInDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/auth/signin", request, SuccessResponseDTO.class);

        return response.getHeaders().getValuesAsList(headers.SET_COOKIE).get(0);
    }

    @Test
    @Order(1)
    @DisplayName("Todo 저장하기")
    void testSaveTodo() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());

        HttpEntity<TodoDTO> request = new HttpEntity<>(todoDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/{id}/todo/save", request, SuccessResponseDTO.class,1);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getResponse()).isNotNull();
        Assertions.assertThat(response.getBody().getResponse()).isEqualTo("Save todo success");
    }

    @Test
    @DisplayName("전체 Todo 목록 가져오기")
    void test_Load_AllData() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoListDTO> response = restTemplate
                .exchange("/api/{id}/todo/search/all", HttpMethod.GET, request , TodoListDTO.class, 1);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("특정 Todo 하나만 가져오기")
    void testGetOneTodo() {

    }

    @Test
    @DisplayName("일치하는 날짜 Todo 가져오기")
    void testGetCalendarTodo() {

    }

    @Test
    @DisplayName("일치하는 상태 Todo 가져오기")
    void testGetStatusTodo() {

    }

    @Test
    @DisplayName("일치하는 카테고리 Todo 가져오기")
    void testGetCategoryTodo() {

    }
}

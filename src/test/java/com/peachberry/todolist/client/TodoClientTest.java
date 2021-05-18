package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.controller.dto.TodoResponse;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.data.AuthorityData;
import com.peachberry.todolist.data.CategoryData;
import com.peachberry.todolist.data.DatabaseCleanup;
import com.peachberry.todolist.data.TodoData;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.domain.TodoStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoClientTest extends AuthUtil {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorityData authorityData;

    @Autowired
    private CategoryData categoryData;

    @Autowired
    private TodoData todoData;

    @Autowired
    private DatabaseCleanup dbCleanUp;

    @BeforeEach
    void setUp() {
        authorityData.saveAuthority(Role.USER);
        userId = signUp(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin(restTemplate));
        this.headers = headers;
    }

    @AfterEach
    void tearDown() {
        dbCleanUp.execute();
    }

    @Test
    @DisplayName("Todo 저장하기")
    void testSaveTodo() {
        TodoRequest.Save saveDto = new TodoRequest.Save("코딩하기", LocalDate.now(), 1L);
        HttpEntity<TodoRequest.Save> request = new HttpEntity<>(saveDto, headers);

        ResponseEntity<TodoResponse.Save> responseEntity = restTemplate
                .postForEntity("/todo/{member-id}",
                        request,
                        TodoResponse.Save.class,
                        1);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("전체 Todo 목록 가져오기")
    void testLoadAllData() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        todoData.saveTodo(userId, categoryId, LocalDate.now(), "밥 먹기", TodoStatus.ING);
        todoData.saveTodo(userId, categoryId, LocalDate.now(), "학교가기", TodoStatus.ING);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfoList> response = restTemplate
                .exchange("/todo/{member-id}/all",
                        HttpMethod.GET,
                        request,
                        TodoResponse.TodoInfoList.class,
                        1);

        TodoResponse.TodoInfoList result = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getTodoInfoList().size()).isGreaterThan(1);
    }


    @Test
    @DisplayName("특정 Todo 하나만 가져오기")
    void testGetOneTodo() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        Long todoId1 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "밥 먹기", TodoStatus.ING);
        Long todoId2 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "학교가기", TodoStatus.ING);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfo> responseEntity = restTemplate.exchange(
                "/todo/{member-id}/{todo-id}",
                HttpMethod.GET,
                request,
                TodoResponse.TodoInfo.class,
                userId, todoId2);

        TodoResponse.TodoInfo response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getId()).isEqualTo(todoId2);
        Assertions.assertThat(response.getTitle()).isEqualTo("학교가기");

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

    @Test
    @DisplayName("일치하는 날짜로 Todo 가져오기")
    void testGetDateTimeTodo() {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfoList> responseEntity = restTemplate.exchange(
                "/todo/{member-id}?datetime=2021-04-10",
                HttpMethod.GET,
                request,
                TodoResponse.TodoInfoList.class,
                1L);

        TodoResponse.TodoInfoList response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response).isNotNull();
        for(TodoResponse.TodoInfo info : response.getTodoInfoList()) {
            System.out.println(info);
        }
    }

    @Test
    @DisplayName("Todo 날짜 수정하기")
    void testUpdateDate() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        Long todoId1 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "밥 먹기", TodoStatus.ING);
        Long todoId2 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "학교가기", TodoStatus.ING);

        CategoryControllerDto.CategoryInfo categoryInfo = new CategoryControllerDto.CategoryInfo(2L);

        TodoRequest.Update updateDto = new TodoRequest.Update(
                todoId1,
                categoryInfo,
                "밥먹기2",
                "2021-04-10",
                "ING");

        HttpEntity<TodoRequest.Update> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<TodoResponse.Update> responseEntity = restTemplate
                .exchange("/todo/",
                        HttpMethod.PUT,
                        request,
                        TodoResponse.Update.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(todoId1);
    }

    @Test
    @DisplayName("Todo 삭제하기")
    void testDeleteTodo() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        Long todoId1 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "밥 먹기", TodoStatus.ING);
        Long todoId2 = todoData.saveTodo(userId, categoryId, LocalDate.now(), "학교가기", TodoStatus.ING);

        TodoRequest.Delete delete = new TodoRequest.Delete(todoId1);

        HttpEntity<TodoRequest.Delete> request = new HttpEntity<>(delete, headers);

        ResponseEntity<SuccessResponseDTO> responseEntity = restTemplate
                .exchange("/todo",
                        HttpMethod.DELETE,
                        request,
                        SuccessResponseDTO.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getResponse()).isEqualTo("DELETE");
    }


}

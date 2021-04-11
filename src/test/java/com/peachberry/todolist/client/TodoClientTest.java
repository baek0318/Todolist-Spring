package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.controller.dto.TodoResponse;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoClientTest extends SignIn{

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin(restTemplate));
        this.headers = headers;
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

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<TodoResponse.TodoInfo> responseEntity = restTemplate.exchange(
                "/todo/{member-id}/{todo-id}",
                HttpMethod.GET,
                request,
                TodoResponse.TodoInfo.class,
                1L,2L);

        TodoResponse.TodoInfo response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getId()).isEqualTo(2L);
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
        CategoryControllerDto.CategoryInfo categoryInfo = new CategoryControllerDto.CategoryInfo(2L);
        TodoRequest.Update updateDto = new TodoRequest.Update(
                1L,
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
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Todo 삭제하기")
    void testDeleteTodo() {

        TodoRequest.Delete delete = new TodoRequest.Delete(2L);

        HttpEntity<TodoRequest.Delete> request = new HttpEntity<>(delete, headers);

        ResponseEntity<SuccessResponseDTO> responseEntity = restTemplate
                .exchange("/todo",
                        HttpMethod.DELETE,
                        request,
                        SuccessResponseDTO.class,
                        1L);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getResponse()).isEqualTo("DELETE");
    }


}

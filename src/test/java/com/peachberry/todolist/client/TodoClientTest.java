package com.peachberry.todolist.client;

import com.peachberry.todolist.dto.TodoListDTO;
import com.peachberry.todolist.dto.request.SignInDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");
    //Todo 전체 Todo목록 가져오기 (load)
    //Todo 특정 카테고리 목록 가져오기
    //Todo 선택한 Todo정보 가져오기 (add)
    //Todo 작성 / 초기에 등록할 때 카테고리 지정 (1개만 가능) / 초기에 등록할 때 날짜 지정 (년 월 일) (NotNull)
    //Todo 수정 (update)
    //Todo 완료 (update)
    //Todo 삭제 (delete)
    //Todo 날짜로 검색 (search)
    //Todo 완료로 검색 (search)
    //Todo 카테고리로 검색 (search)

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
}

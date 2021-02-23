package com.peachberry.todolist.client;

import com.peachberry.todolist.dto.CategoryDTO;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class CategoryClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");

    private final CategoryDTO categoryDTO = CategoryDTO.builder()
            .title("하루일과")
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
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());

        HttpEntity<CategoryDTO> request = new HttpEntity<>(categoryDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/{id}/category/save", request, SuccessResponseDTO.class,1);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getResponse()).isEqualTo("Save category success");
    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());


    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());
    }

    @Test
    @DisplayName("해당 카테고리 업데이트하기")
    void testUpdateCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());
    }

    @Test
    @DisplayName("해당 카테고리 삭제하기")
    void testDeleteCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());
    }
}

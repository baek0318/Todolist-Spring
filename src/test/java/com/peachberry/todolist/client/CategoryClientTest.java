package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final SignInDTO signInDTO = new SignInDTO("peachberry@kakao.com", "1234");

    private final CategoryControllerDto.Save categorySaveDTO = CategoryControllerDto.Save.builder()
            .title("하루일과")
            .build();

    private HttpHeaders headers;

    private String signin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<SignInDTO> request = new HttpEntity<>(signInDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/auth/signin", request, SuccessResponseDTO.class);

        return response.getHeaders().getValuesAsList(headers.SET_COOKIE).get(0);
    }

    @BeforeEach
    void setUp() {
        String token = signin();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin());
        this.headers = headers;
    }

    @Test
    @Order(1)
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {

        HttpEntity<CategoryControllerDto.Save> request = new HttpEntity<>(categorySaveDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/api/{id}/category/save", request, SuccessResponseDTO.class,1);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getResponse()).isEqualTo("Save category success");
    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryControllerDto.CategoryList> response = restTemplate
                .exchange("/api/{id}/category/search/all",
                        HttpMethod.GET,
                        request,
                        CategoryControllerDto.CategoryList.class,
                        1);


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryControllerDto.CategoryList> response = restTemplate
                .exchange("/api/{id}/category/search/all",
                        HttpMethod.GET,
                        request,
                        CategoryControllerDto.CategoryList.class,
                        1);

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

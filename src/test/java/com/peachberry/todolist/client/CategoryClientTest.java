package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.CategoryResponse;
import com.peachberry.todolist.controller.dto.auth.SignInDTO;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryClientTest extends SignIn {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    @Override
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", signin(restTemplate));
        this.headers = headers;
    }


    @Test
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {
        CategoryControllerDto.Save categorySaveDTO = CategoryControllerDto.Save.builder()
                .title("하루일과2")
                .build();

        HttpEntity<CategoryControllerDto.Save> request = new HttpEntity<>(categorySaveDTO, headers);

        ResponseEntity<CategoryResponse.Save> response = restTemplate
                .postForEntity("/category/{member-id}", request, CategoryResponse.Save.class,1L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryResponse.CategoryList> responseEntity = restTemplate
                .exchange("/category/{member-id}/all",
                        HttpMethod.GET,
                        request,
                        CategoryResponse.CategoryList.class,
                        1L);

        CategoryResponse.CategoryList response = responseEntity.getBody();

        for(CategoryResponse.CategoryInfo info : response.getCategoryList()) {
            System.out.println("info id : "+info.getId() +" info title : "+ info.getTitle());
        }
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryResponse.CategoryInfo> responseEntity = restTemplate
                .exchange("/category/{member-id}?title=하루일과3",
                        HttpMethod.GET,
                        request,
                        CategoryResponse.CategoryInfo.class,
                        1);

        CategoryResponse.CategoryInfo info = responseEntity.getBody();
        Assertions.assertThat(info.getTitle()).isEqualTo("하루일과3");
        Assertions.assertThat(info.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("해당 카테고리 업데이트하기")
    void testUpdateCategory() {
        CategoryControllerDto.Update update = new CategoryControllerDto.Update(2L, "하루일과4");
        HttpEntity<CategoryControllerDto.Update> request = new HttpEntity<>(update, headers);

        ResponseEntity<CategoryResponse.Update> responseEntity = restTemplate
                .exchange("/category/{member-id}",
                        HttpMethod.PUT,
                        request,
                        CategoryResponse.Update.class,
                        1L);

        CategoryResponse.Update updated = responseEntity.getBody();
        Assertions.assertThat(updated.getId()).isEqualTo(2L);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("해당 카테고리 삭제하기")
    void testDeleteCategory() {
        CategoryControllerDto.Delete delete = new CategoryControllerDto.Delete(3L);
        HttpEntity<CategoryControllerDto.Delete> request = new HttpEntity<>(delete, headers);

        ResponseEntity<SuccessResponseDTO> responseEntity = restTemplate.exchange(
                "/category/{member-id}",
                HttpMethod.DELETE,
                request,
                SuccessResponseDTO.class,
                1L);

        SuccessResponseDTO response = responseEntity.getBody();

        Assertions.assertThat(response.getResponse()).isEqualTo("DELETE");
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


}

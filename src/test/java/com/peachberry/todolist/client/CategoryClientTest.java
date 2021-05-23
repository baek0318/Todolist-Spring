package com.peachberry.todolist.client;

import com.peachberry.todolist.data.AuthorityData;
import com.peachberry.todolist.data.CategoryData;
import com.peachberry.todolist.data.DatabaseCleanup;
import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.CategoryResponse;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryClientTest extends AuthUtil {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private AuthorityData authorityData;

    @Autowired
    private CategoryData categoryData;

    @BeforeEach
    @Override
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
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {
        CategoryControllerDto.Save categorySaveDTO = CategoryControllerDto.Save.builder()
                .title("하루일과2")
                .build();

        HttpEntity<CategoryControllerDto.Save> request = new HttpEntity<>(categorySaveDTO, headers);

        ResponseEntity<CategoryResponse.Save> response = restTemplate
                .postForEntity(
                        "/category/{member-id}",
                        request,
                        CategoryResponse.Save.class,
                        1L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {
        Long categoryId1 = categoryData.saveCategory("하루일과", userId);
        Long categoryId2 = categoryData.saveCategory("하루일과2", userId);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryResponse.CategoryList> responseEntity = restTemplate
                .exchange(
                        "/category/{member-id}/all",
                        HttpMethod.GET,
                        request,
                        CategoryResponse.CategoryList.class,
                        1L);

        CategoryResponse.CategoryList response = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        for(int i = 1; i <= response.getCategoryList().size(); i++) {
            Assertions.assertThat(response.getCategoryList().get(i-1).getId()).isEqualTo(i);
        }
        Assertions.assertThat(responseEntity.getBody().getCategoryList()).isNotNull();
    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryResponse.CategoryInfo> responseEntity = restTemplate
                .exchange("/category/{member-id}?title=하루일과",
                        HttpMethod.GET,
                        request,
                        CategoryResponse.CategoryInfo.class,
                        userId);

        CategoryResponse.CategoryInfo info = responseEntity.getBody();
        Assertions.assertThat(info.getTitle()).isEqualTo("하루일과");
        Assertions.assertThat(info.getId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("해당 카테고리 업데이트하기")
    void testUpdateCategory() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        CategoryControllerDto.Update update = new CategoryControllerDto.Update(categoryId, "하루일과4");
        HttpEntity<CategoryControllerDto.Update> request = new HttpEntity<>(update, headers);

        ResponseEntity<CategoryResponse.Update> responseEntity = restTemplate
                .exchange("/category/{member-id}",
                        HttpMethod.PUT,
                        request,
                        CategoryResponse.Update.class,
                        userId);

        CategoryResponse.Update updated = responseEntity.getBody();
        Assertions.assertThat(updated.getId()).isEqualTo(categoryId);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("해당 카테고리 삭제하기")
    void testDeleteCategory() {
        Long categoryId = categoryData.saveCategory("하루일과", userId);
        CategoryControllerDto.Delete delete = new CategoryControllerDto.Delete(categoryId);
        HttpEntity<CategoryControllerDto.Delete> request = new HttpEntity<>(delete, headers);

        ResponseEntity<SuccessResponseDTO> responseEntity = restTemplate.exchange(
                "/category/{member-id}",
                HttpMethod.DELETE,
                request,
                SuccessResponseDTO.class,
                userId);

        SuccessResponseDTO response = responseEntity.getBody();

        Assertions.assertThat(response.getResponse()).isEqualTo("DELETE");
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


}

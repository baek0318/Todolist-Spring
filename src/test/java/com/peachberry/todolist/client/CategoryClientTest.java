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
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final SignInDTO signInDTO = new SignInDTO("peachberry2@kakao.com", "1234");

    private final CategoryControllerDto.Save categorySaveDTO = CategoryControllerDto.Save.builder()
            .title("하루일과2")
            .build();

    private HttpHeaders headers;

    private String signin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<SignInDTO> request = new HttpEntity<>(signInDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/auth/signin", request, SuccessResponseDTO.class);
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
    @DisplayName("카테고리 저장하기")
    void testSaveCategory() {

        HttpEntity<CategoryControllerDto.Save> request = new HttpEntity<>(categorySaveDTO, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/category/{member-id}", request, SuccessResponseDTO.class,1L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getResponse()).isEqualTo("Save category success");
    }

    @Test
    @DisplayName("모든 카테고리 검색하기")
    void testFindAllCategory() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryControllerDto.CategoryList> response = restTemplate
                .exchange("/category/{member-id}/all",
                        HttpMethod.GET,
                        request,
                        CategoryControllerDto.CategoryList.class,
                        1L);


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("특정 제목으로 카테고리 검색하기")
    void testFindCategoryByTitle() {

        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<CategoryControllerDto.CategoryInfo> responseEntity = restTemplate
                .exchange("/category/{member-id}?title=하루일과",
                        HttpMethod.GET,
                        request,
                        CategoryControllerDto.CategoryInfo.class,
                        1);

        CategoryControllerDto.CategoryInfo info = responseEntity.getBody();
        Assertions.assertThat(info.getTitle()).isEqualTo("하루일과");
        Assertions.assertThat(info.getId()).isEqualTo(1L);
    }
/*
    @Test
    @DisplayName("해당 카테고리 업데이트하기")
    void testUpdateCategory() {

    }

    @Test
    @DisplayName("해당 카테고리 삭제하기")
    void testDeleteCategory() {

    }
 */

}

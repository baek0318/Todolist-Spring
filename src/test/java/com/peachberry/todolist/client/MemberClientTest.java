package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.MemberResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberClientTest extends SignIn {

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
    @DisplayName("회원정보 가져오기")
    void testGetMemberDetail() {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<MemberResponse.MemberInfo> responseEntity = restTemplate
                .exchange(
                        "/member/{member-id}",
                        HttpMethod.GET,
                        request,
                        MemberResponse.MemberInfo.class,
                        1L
                        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(1L);
    }
}

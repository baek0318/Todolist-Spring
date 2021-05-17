package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.MemberResponse;

import com.peachberry.todolist.data.AuthorityData;
import com.peachberry.todolist.data.DatabaseCleanup;
import com.peachberry.todolist.data.MemberData;
import com.peachberry.todolist.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberClientTest extends AuthUtil {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DatabaseCleanup dbClean;

    @Autowired
    private MemberData memberData;

    @Autowired
    private AuthorityData authorityData;

    @AfterEach
    void tearDown() {
        dbClean.execute();
    }

    @BeforeEach
    @Override
    void setUp() {
        authorityData.saveAuthority(Role.USER);
        userId = memberData.saveMember(
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getName()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
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
                        userId
                        );
        System.out.println(userId);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(1L);
        Assertions.assertThat(responseEntity.getBody().getEmail()).isEqualTo(signUpRequest.getEmail());
        Assertions.assertThat(responseEntity.getBody().getName()).isEqualTo(signUpRequest.getName());
    }
}

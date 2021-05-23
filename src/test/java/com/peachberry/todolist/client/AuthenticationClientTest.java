package com.peachberry.todolist.client;

import com.peachberry.todolist.controller.dto.MemberResponse;
import com.peachberry.todolist.data.AuthorityData;
import com.peachberry.todolist.data.DatabaseCleanup;
import com.peachberry.todolist.data.MemberData;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.controller.dto.auth.SignInRequest;
import com.peachberry.todolist.controller.dto.auth.SignUpRequest;
import com.peachberry.todolist.controller.dto.auth.SignUpResponse;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.security.cookie.CookieUtil;
import com.peachberry.todolist.security.jwt.JwtUtil;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationClientTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private AuthorityData authorityData;

    @Autowired
    private MemberData memberData;

    @Autowired
    private DatabaseCleanup dbClean;

    @PersistenceContext
    private EntityManager em;

    private final SignUpRequest signUpRequest = new SignUpRequest("peach@kakao.com", "peachberry", "1234");

    private final SignInRequest signInRequest = new SignInRequest("peach@kakao.com", "1234");

    private final Logger logger = LoggerFactory.getLogger(AuthenticationClientTest.class);

    private String refresh = "empty";

    @BeforeEach
    void setUp() {
        authorityData.saveAuthority(Role.USER);
    }

    @AfterEach
    void tearDown() {
        dbClean.execute();
    }

    @Test
    @DisplayName("회원가입 통합 테스트")
    void testSignUp() {
        //given
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest, headers);

        ResponseEntity<SignUpResponse> response = restTemplate
                .postForEntity("/auth/signup",request , SignUpResponse.class);
        System.out.println("ID : "+response.getBody().getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 통합 테스트")
    void testSignIn() {
        //given
        Long userId = memberData.saveMember("peach@kakao.com", "1234", "peach");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<SignInRequest> request = new HttpEntity<>(signInRequest, headers);

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .postForEntity("/auth/signin", request, SuccessResponseDTO.class);

        List<String> cookies = response.getHeaders().getValuesAsList(headers.SET_COOKIE);
        String access_header = cookies.get(0).split("=")[0]; //access_header
        String access_token = cookies.get(0).split(";")[0].split("=")[1]; //access_header
        String refresh_header = cookies.get(2).split("=")[0]; //refresh_token
        String refresh_token = cookies.get(2).split(";")[0].split("=")[1]; //refresh_token

        System.out.println("ID : "+userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(access_header).isEqualTo("ACCESS-TOKEN");
        assertThat(refresh_header).isEqualTo("REFRESH-TOKEN");
        assertThat(jwtUtil.getEmailFromJwtToken(access_token)).isEqualTo(signInRequest.getEmail());
        assertThat(jwtUtil.getEmailFromJwtToken(refresh_token)).isEqualTo(signInRequest.getEmail());
    }

    @Test
    @DisplayName("로그아웃 통합 테스트")
    void testSignOut() {

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .getForEntity("/auth/signout",SuccessResponseDTO.class);

        List<String> cookies = response.getHeaders().getValuesAsList("Set-Cookie");
        String access_header = cookies.get(0).split("=")[0]; //access_header
        String access_age = cookies.get(0).split(";")[1].split("=")[1]; //access_age
        String refresh_header = cookies.get(2).split("=")[0]; //refresh_header
        String refresh_age = cookies.get(2).split(";")[1].split("=")[1]; //refresh_age

        assertThat(access_header).isEqualTo("ACCESS-TOKEN");
        assertThat(refresh_header).isEqualTo("REFRESH-TOKEN");
        assertThat(access_age).isEqualTo("0");
        assertThat(refresh_age).isEqualTo("0");
    }

    @Test
    @DisplayName("ACCESS 토큰 재발행하기 통합테스트")
    void testIssueAccessToken() {
        Long userId = memberData.saveMember("peach@kakao.com", "1234", "peach");

        HttpHeaders headers2 = new HttpHeaders();

        HttpEntity<SignInRequest> request = new HttpEntity<>(signInRequest, headers2);

        ResponseEntity<SuccessResponseDTO> response2 = restTemplate
                .postForEntity("/auth/signin", request, SuccessResponseDTO.class);

        List<String> cookies2 = response2.getHeaders().getValuesAsList(headers2.SET_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookies2.get(2));

        ResponseEntity<SuccessResponseDTO> response = restTemplate
                .exchange("/auth/issueAccess", HttpMethod.GET, new HttpEntity<>(headers) ,SuccessResponseDTO.class);

        List<String> cookies = response.getHeaders().getValuesAsList("Set-Cookie");
        String access_header = cookies.get(0).split("=")[0]; //access_header
        String access_age = cookies.get(0).split(";")[1].split("=")[1]; //access_age

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(access_header).isEqualTo("ACCESS-TOKEN");
        assertThat(Integer.parseInt(access_age)).isGreaterThan(Integer.parseInt("0"));
    }
}

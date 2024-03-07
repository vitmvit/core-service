package ru.clevertec.news.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.util.AuthTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class AuthClientTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private AuthClient authClient;

    @Test
    public void signUpShouldReturnJwtToken() throws JsonProcessingException {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        JwtDto jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        stubFor(post(urlPathEqualTo("/api/auth/signUp"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(signUpDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(jwtDto))));

        var actual = authClient.signUp(signUpDto);

        Assertions.assertEquals(jwtDto, actual);
    }

    @Test
    public void signUpShouldReturnException() throws JsonProcessingException {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        stubFor(post(urlPathEqualTo("/api/auth/signUp"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(signUpDto))
                )
                .willReturn(aResponse()
                        .withStatus(302)));

        assertThrows(FeignException.class, () -> authClient.signUp(signUpDto));
    }

    @Test
    public void signInShouldReturnJwtToken() throws JsonProcessingException {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();
        JwtDto jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        stubFor(post(urlPathEqualTo("/api/auth/signIn"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(signInDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(jwtDto))));

        var actual = authClient.signIn(signInDto);

        Assertions.assertEquals(jwtDto, actual);
    }

    @Test
    public void signInShouldReturnException() throws JsonProcessingException {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        stubFor(post(urlPathEqualTo("/api/auth/signIn"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(signInDto))
                )
                .willReturn(aResponse()
                        .withStatus(302)));

        assertThrows(FeignException.class, () -> authClient.signIn(signInDto));
    }
}


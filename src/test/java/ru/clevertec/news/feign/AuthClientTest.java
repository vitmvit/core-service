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
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.util.AuthTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThrows;
import static ru.clevertec.news.constant.Constant.AUTHORIZATION_HEADER;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
public class AuthClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthClient authClient;

    @Test
    public void signUpShouldReturnJwtToken() throws JsonProcessingException {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        var jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

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
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

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
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();
        var jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

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
    public void checkShouldReturnJwtToken() {
        var jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        stubFor(post(urlPathEqualTo("/api/auth/check"))
                .withHeader(AUTHORIZATION_HEADER, equalTo(jwtDto.accessToken()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(String.valueOf(true))));

        var actual = authClient.check(jwtDto.accessToken());

        Assertions.assertTrue(actual);
    }
}
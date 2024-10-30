package ru.clevertec.news.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.util.CommentTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
public class CommentClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentClient commentClient;

    @Test
    public void getCommentByIdShouldReturnExpectedCommentDto() throws JsonProcessingException {
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        var id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/comments/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));

        var actual = commentClient.getById(id);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void getCommentByIdShouldReturnExceptionAndStatus404() {
        var id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/comments/{id}"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> commentClient.getById(id));
    }

    @Test
    public void createCommentShouldReturnExpectedCommentDto() throws JsonProcessingException {
        var commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        stubFor(post(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));

        var actual = commentClient.create(commentCreateDto);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void createCommentShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        var commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();

        stubFor(post(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.create(commentCreateDto));
    }

    @Test
    public void updateCommentShouldReturnExpectedCommentDto() throws JsonProcessingException {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(commentUpdateDto)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));


        var actual = commentClient.update(commentUpdateDto);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void updateCommentShouldReturnExceptionAndStatus404() throws JsonProcessingException {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> commentClient.update(commentUpdateDto));
    }

    @Test
    public void updateCommentShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.update(commentUpdateDto));
    }

    @Test
    public void deleteCommentShouldReturnStatus200() {
        var id = CommentTestBuilder.builder().build().buildCommentDto().getId();

        stubFor(delete(urlPathTemplate("/api/comments/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        assertDoesNotThrow(() -> commentClient.delete(id));
    }

    @Test
    public void deleteCommentShouldReturnExceptionAndStatus403() {
        var id = CommentTestBuilder.builder().build().buildCommentDto().getId();

        stubFor(delete(urlPathTemplate("/api/comments/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.delete(id));
    }
}
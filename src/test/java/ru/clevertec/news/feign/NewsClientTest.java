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
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.util.CommentTestBuilder;
import ru.clevertec.news.util.NewsTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;


@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class NewsClientTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private NewsClient newsClient;

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDto() throws JsonProcessingException {
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        Long id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.getNewsById(id);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() {
        Long id = NewsTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    public void getByIdWithCommentsShouldReturnExpectedNewsDto() throws JsonProcessingException {
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        Long id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}/comments"))
                .withQueryParam("offset", matching("^(.*)([0-9]+)$"))
                .withQueryParam("limit", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.getByIdWithComments(OFFSET, LIMIT, id);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
    }

    @Test
    public void getByIdWithCommentsShouldReturnExceptionAndStatus404() {
        Long id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}/comments"))
                .withQueryParam("offset", matching("^(.*)([0-9]+)$"))
                .withQueryParam("limit", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.getByIdWithComments(OFFSET, LIMIT, id));
    }

    @Test
    public void createNewsShouldReturnExpectedNewsDto() throws JsonProcessingException {
        NewsCreateDto newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(post(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.createNews(newsCreateDto, token);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUserId(), actual.getUserId());
    }

    @Test
    public void createNewsShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        NewsCreateDto newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(post(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.createNews(newsCreateDto, token));
    }

    @Test
    public void updateNewsShouldReturnExpectedCommentDto() throws JsonProcessingException {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.updateNews(newsUpdateDto, token);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUserId(), actual.getUserId());
    }

    @Test
    public void updateNewsShouldReturnExceptionAndStatus404() throws JsonProcessingException {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.updateNews(newsUpdateDto, token));
    }

    @Test
    public void updateNewsShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.updateNews(newsUpdateDto, token));
    }

    @Test
    public void deleteNewsShouldReturnStatus200() {
        Long id = NewsTestBuilder.builder().build().getId();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(delete(urlPathTemplate("/api/news/{id}/{userId}"))
                .withHeader("Authorization", equalTo(token))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .withPathParam("userId", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        assertDoesNotThrow(() -> newsClient.deleteNews(id, id, token));
    }

    @Test
    public void deleteNewsShouldReturnExceptionAndStatus403() {
        Long id = NewsTestBuilder.builder().build().getId();
        String token = NewsTestBuilder.builder().build().getToken();

        stubFor(delete(urlPathTemplate("/api/news/{id}/{userId}"))
                .withHeader("Authorization", equalTo(token))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .withPathParam("userId", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.deleteNews(id, id, token));
    }
}
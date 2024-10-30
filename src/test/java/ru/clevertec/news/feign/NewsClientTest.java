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
import ru.clevertec.news.util.NewsTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;


@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
public class NewsClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NewsClient newsClient;

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDto() throws JsonProcessingException {
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var id = CommentTestBuilder.builder().build().getId();

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
        var id = NewsTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    public void getByIdWithCommentsShouldReturnExpectedNewsDto() throws JsonProcessingException {
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}/comments"))
                .withQueryParam("pageNumber", matching("^(.*)([0-9]+)$"))
                .withQueryParam("pageSize", matching("^(.*)([0-9]+)$"))
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
        var id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/news/{id}/comments"))
                .withQueryParam("pageNumber", matching("^(.*)([0-9]+)$"))
                .withQueryParam("pageSize", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.getByIdWithComments(OFFSET, LIMIT, id));
    }

    @Test
    public void createNewsShouldReturnExpectedNewsDto() throws JsonProcessingException {
        var newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        stubFor(post(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.create(newsCreateDto);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUserId(), actual.getUserId());
    }

    @Test
    public void createShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        var newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();

        stubFor(post(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.create(newsCreateDto));
    }

    @Test
    public void updateShouldReturnExpectedCommentDto() throws JsonProcessingException {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(newsDto))));

        var actual = newsClient.update(newsUpdateDto);

        assertEquals(newsDto.getId(), actual.getId());
        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUserId(), actual.getUserId());
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws JsonProcessingException {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> newsClient.update(newsUpdateDto));
    }

    @Test
    public void updateShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        stubFor(put(urlPathTemplate("/api/news"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(newsUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.update(newsUpdateDto));
    }

    @Test
    public void deleteShouldReturnStatus200() {
        var id = NewsTestBuilder.builder().build().getId();

        stubFor(delete(urlPathTemplate("/api/news/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        assertDoesNotThrow(() -> newsClient.delete(id));
    }

    @Test
    public void deleteShouldReturnExceptionAndStatus403() {
        var id = NewsTestBuilder.builder().build().getId();

        stubFor(delete(urlPathTemplate("/api/news/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> newsClient.delete(id));
    }
}
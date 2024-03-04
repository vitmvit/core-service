package ru.clevertec.news.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.NewsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsService newsService;

    @Autowired
    private AuthClient authClient;

    @Test
    public void getNewsByIdWithCommentsShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;

        var actual = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 200L;

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;

        var actual = newsService.findNewsById(id);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 200L;

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllNewsShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        var actual = newsService.findAllNews(OFFSET, LIMIT);

        mockMvc.perform(get("/api/news?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void getAllNewsShouldReturnExceptionAndStatus404() throws Exception {
        Integer page = 100;

        mockMvc.perform(get("/api/news?offset=" + page + "&limit=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTextShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "t";

        var actual = newsService.searchNewsByText(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void searchNewsByTextShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "0";

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTitleShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "n";

        var actual = newsService.searchNewsByTitle(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
        ;
    }

    @Test
    public void searchNewsByTitleShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "0";
        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void createNewsShouldReturnCreatedNewsAndStatus200() throws Exception {
        SignInDto signInDto = new SignInDto("smarthacker76", "smarthacker76");
        Long userId = 6L;
        var token = authClient.signIn(signInDto);

        NewsCreateDto newsCreateDto = new NewsCreateDto("title", "text", userId);

        mockMvc.perform(post("/api/news")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNewsShouldReturnUpdatedNewsAndStatus201() throws Exception {
        SignInDto signInDto = new SignInDto("smarthacker76", "smarthacker76");
        Long userId = 6L;
        var token = authClient.signIn(signInDto);

        NewsCreateDto newsCreateDto = new NewsCreateDto("title", "text", userId);
        var news = newsService.createNews(newsCreateDto, token.accessToken());

        NewsUpdateDto newsUpdateDto = new NewsUpdateDto(news.getId(), news.getTitle(), news.getText(), news.getUserId());
        var actual = newsService.updateNews(newsUpdateDto, token.accessToken());

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void updateNewsShouldReturnException() throws Exception {
        SignInDto signInDto = new SignInDto("smarthacker76", "smarthacker76");
        Long userId = 6L;
        var token = authClient.signIn(signInDto);

        NewsCreateDto newsCreateDto = new NewsCreateDto("title", "text", userId);
        var news = newsService.createNews(newsCreateDto, token.accessToken());

        NewsUpdateDto newsUpdateDto = new NewsUpdateDto(news.getId(), news.getTitle(), news.getText(), news.getUserId());

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteNewsShouldReturnStatus204() throws Exception {
        SignInDto signInDto = new SignInDto("smarthacker76", "smarthacker76");
        Long userId = 6L;
        var token = authClient.signIn(signInDto);

        NewsCreateDto newsCreateDto = new NewsCreateDto("title", "text", userId);
        var news = newsService.createNews(newsCreateDto, token.accessToken());
        Long newsId = news.getId();

        mockMvc.perform(delete("/api/news/" + newsId + "/" + userId)
                        .header("Authorization", token.accessToken()))
                .andExpect(status().isNoContent());
    }
}

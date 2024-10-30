package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NewsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    public void getByIdWithCommentsShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        var id = 1L;
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.getByIdWithComments(OFFSET, LIMIT, id)).thenReturn(newsDto);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(status().isOk());
    }

    @Test
    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
        var id = 20L;

        when(newsService.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        var id = 1L;
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.getNewsById(id)).thenReturn(newsDto);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
        var id = 20L;

        when(newsService.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        var pageContent = new PageContentDto<>(
                new PageDto(1, 10, 100, 1000L),
                List.of(NewsTestBuilder.builder().build().buildNewsDto())
        );

        when(newsService.getAll(OFFSET, LIMIT, null, null)).thenReturn(pageContent);

        mockMvc.perform(get("/api/news?pageNumber=" + OFFSET + "&pageSize=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageContent)));
    }

    @Test
    public void createShouldReturnCreatedNewsAndStatus200() throws Exception {
        var newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.create(newsCreateDto)).thenReturn(newsDto);

        mockMvc.perform(post("/api/news")
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createShouldReturnException() throws Exception {
        var newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();

        when(newsService.create(newsCreateDto)).thenThrow(NoAccessError.class);

        mockMvc.perform(post("/api/news/")
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
    }

    @Test
    public void updateShouldReturnUpdatedNewsAndStatus201() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.update(newsUpdateDto)).thenReturn(newsDto);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        newsUpdateDto.setId(100L);

        when(newsService.update(newsUpdateDto)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void updateShouldReturnException() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        when(newsService.update(newsUpdateDto)).thenThrow(NoAccessError.class);

        mockMvc.perform(post("/api/news/")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
    }

    @Test
    public void deleteShouldReturnStatus204() throws Exception {
        var id = 2L;

        mockMvc.perform(delete("/api/news/" + id))
                .andExpect(status().isNoContent());

        verify(newsService).delete(id);
    }
}
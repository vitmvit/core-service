package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    public void getNewsByIdWithCommentsShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.findNewsByIdWithComments(OFFSET, LIMIT, id)).thenReturn(newsDto);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(status().isOk());
    }

    @Test
    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        when(newsService.findNewsById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();

        when(newsService.findNewsById(id)).thenReturn(newsDto);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        when(newsService.findNewsById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllNewsShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        List<NewsDto> newsDtoList = List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        );
        Page<NewsDto> page = PageableExecutionUtils.getPage(
                newsDtoList,
                PageRequest.of(OFFSET, LIMIT),
                newsDtoList::size);

        when(newsService.findAllNews(OFFSET, LIMIT)).thenReturn(page);

        mockMvc.perform(get("/api/news?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllNewsShouldReturnExceptionAndStatus404() throws Exception {
        Integer page = 100;

        when(newsService.findAllNews(OFFSET, LIMIT)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/news?offset=" + page + "&limit=" + LIMIT))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTextShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "t";
        List<NewsDto> newsDtoList = List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        );
        Page<NewsDto> page = PageableExecutionUtils.getPage(
                newsDtoList,
                PageRequest.of(OFFSET, LIMIT),
                newsDtoList::size);

        when(newsService.searchNewsByText(OFFSET, LIMIT, fragment)).thenReturn(page);

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchNewsByTextShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        when(newsService.searchNewsByText(OFFSET, LIMIT, fragment)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTitleShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "n";
        List<NewsDto> newsDtoList = List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        );
        Page<NewsDto> page = PageableExecutionUtils.getPage(
                newsDtoList,
                PageRequest.of(OFFSET, LIMIT),
                newsDtoList::size);
        when(newsService.searchNewsByTitle(OFFSET, LIMIT, fragment)).thenReturn(page);


        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchNewsByTitleShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        when(newsService.searchNewsByTitle(OFFSET, LIMIT, fragment)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void createNewsShouldReturnCreatedNewsAndStatus200() throws Exception {
        NewsCreateDto newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        when(newsService.createNews(newsCreateDto, token)).thenReturn(newsDto);

        mockMvc.perform(post("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNewsShouldReturnUpdatedNewsAndStatus201() throws Exception {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        when(newsService.updateNews(newsUpdateDto, token)).thenReturn(newsDto);

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNewsShouldReturnExceptionAndStatus404() throws Exception {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        newsUpdateDto.setId(100L);
        String token = NewsTestBuilder.builder().build().getToken();

        when(newsService.updateNews(newsUpdateDto, token)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteNewsShouldReturnStatus204() throws Exception {
        Long id = 2L;
        Long userId = 2L;

        String token = NewsTestBuilder.builder().build().getToken();

        mockMvc.perform(delete("/api/news/" + id + "/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(newsService).deleteNews(id, userId, token);
    }
}


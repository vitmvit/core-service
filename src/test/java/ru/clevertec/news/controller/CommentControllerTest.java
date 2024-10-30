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
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.CommentTestBuilder;

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
public class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void getByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        var id = commentDto.getId();

        when(commentService.getById(id)).thenReturn(commentDto);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdShouldReturnException() throws Exception {
        var id = 20L;

        when(commentService.getById(id)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        var pageContent = new PageContentDto<>(
                new PageDto(1, 10, 100, 1000L),
                List.of(CommentTestBuilder.builder().build().buildCommentDto())
        );

        when(commentService.getAll(OFFSET, LIMIT, null, null)).thenReturn(pageContent);

        mockMvc.perform(get("/api/comments?pageNumber=" + OFFSET + "&pageSize=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageContent)));
    }

    @Test
    public void createShouldReturnCreatedCommentAndStatus201() throws Exception {
        var commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentService.create(commentCreateDto)).thenReturn(commentDto);

        mockMvc.perform(post("/api/comments")
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createShouldReturnException() throws Exception {
        var commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();

        when(commentService.create(commentCreateDto)).thenThrow(NoAccessError.class);

        mockMvc.perform(post("/api/comments/")
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
    }

    @Test
    public void updateShouldReturnUpdatedCommentAndStatus201() throws Exception {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentService.update(commentUpdateDto)).thenReturn(commentDto);

        mockMvc.perform(put("/api/comments")
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws Exception {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        commentUpdateDto.setId(1000L);

        when(commentService.update(commentUpdateDto)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/comments")
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void updateShouldReturnException() throws Exception {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        when(commentService.update(commentUpdateDto)).thenThrow(NoAccessError.class);

        mockMvc.perform(get("/api/comments/")
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
    }

    @Test
    public void deleteShouldReturnStatus204() throws Exception {
        var id = 2L;

        when(commentService.getById(id)).thenReturn(CommentTestBuilder.builder().build().buildCommentDto());

        mockMvc.perform(delete("/api/comments/" + id))
                .andExpect(status().isNoContent());

        verify(commentService).delete(id);
    }
}
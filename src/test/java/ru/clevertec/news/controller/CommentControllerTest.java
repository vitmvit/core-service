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
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.CommentTestBuilder;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void getCommentByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
        Long id = 1L;
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentService.findCommentById(id)).thenReturn(commentDto);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void getCommentByIdShouldReturnException() throws Exception {
        Long id = 20L;

        when(commentService.findCommentById(id)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void searchCommentsByTextShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
        String fragment = "t";
        List<CommentDto> newsDtoList = List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        );
        Page<CommentDto> page = PageableExecutionUtils.getPage(
                newsDtoList,
                PageRequest.of(OFFSET, LIMIT),
                newsDtoList::size);

        when(commentService.searchCommentsByText(OFFSET, LIMIT, fragment)).thenReturn(page);

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchCommentsByTextShouldReturnException() throws Exception {
        String fragment = "0";

        when(commentService.searchCommentsByText(OFFSET, LIMIT, fragment)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchCommentsByUsernameShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
        String fragment = "t";
        List<CommentDto> newsDtoList = List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        );
        Page<CommentDto> page = PageableExecutionUtils.getPage(
                newsDtoList,
                PageRequest.of(OFFSET, LIMIT),
                newsDtoList::size);

        when(commentService.searchCommentsByUsername(OFFSET, LIMIT, fragment)).thenReturn(page);

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchCommentsByUsernameShouldReturnException() throws Exception {
        String fragment = "0";

        when(commentService.searchCommentsByUsername(OFFSET, LIMIT, fragment)).thenThrow(EmptyListException.class);

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void createCommentShouldReturnCreatedCommentAndStatus201() throws Exception {
        CommentCreateDto commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        String token = CommentTestBuilder.builder().build().getToken();
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentService.createComment(commentCreateDto, token)).thenReturn(commentDto);

        mockMvc.perform(post("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCommentShouldReturnUpdatedCommentAndStatus201() throws Exception {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentService.updateComment(commentUpdateDto, token)).thenReturn(commentDto);

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCommentShouldReturnExceptionAndStatus404() throws Exception {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        commentUpdateDto.setId(1000L);
        String token = CommentTestBuilder.builder().build().getToken();

        when(commentService.updateComment(commentUpdateDto, token)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteCommentShouldReturnStatus204() throws Exception {
        Long id = 2L;
        Long userId = 2L;
        String token = CommentTestBuilder.builder().build().getToken();

        mockMvc.perform(delete("/api/comments/" + id + "/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(id, userId, token);
    }
}

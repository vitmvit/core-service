package ru.clevertec.news.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.CommentService;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthClient authClient;

    @Test
    public void getCommentByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
        Long id = 1L;

        var actual = commentService.findCommentById(id);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void getCommentByIdShouldReturnException() throws Exception {
        Long id = 20L;

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void searchCommentsByTextShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
        String fragment = "t";

        var actual = commentService.searchCommentsByText(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void searchCommentsByTextShouldReturnException() throws Exception {
        String fragment = "0";

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchCommentsByUsernameShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
        String fragment = "t";

        var actual = commentService.searchCommentsByUsername(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    public void searchCommentsByUsernameShouldReturnException() throws Exception {
        String fragment = "0";

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    public void createCommentShouldReturnCreatedCommentAndStatus201() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.SUBSCRIBER);
        var token = authClient.signUp(signUpDto);

        CommentCreateDto commentCreateDto = new CommentCreateDto("text", "login1", 1L);

        mockMvc.perform(post("/api/comments")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    public void updateCommentShouldReturnUpdatedCommentAndStatus201() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.SUBSCRIBER);
        var token = authClient.signUp(signUpDto);

        CommentCreateDto commentCreateDto = new CommentCreateDto("text", "login1", 1L);
        var comment = commentService.createComment(commentCreateDto, token.accessToken());

        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(comment.getId());
        commentUpdateDto.setText(comment.getText());
        commentUpdateDto.setUsername(comment.getUsername());
        commentUpdateDto.setNewsId(comment.getNewsId());
        var actual = commentService.updateComment(commentUpdateDto, token.accessToken());

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actual)));
    }

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    public void updateCommentShouldReturnException() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.SUBSCRIBER);
        var token = authClient.signUp(signUpDto);

        CommentCreateDto commentCreateDto = new CommentCreateDto("text", "login1", 100L);
        var comment = commentService.createComment(commentCreateDto, token.accessToken());

        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(comment.getId());
        commentUpdateDto.setText(comment.getText());
        commentUpdateDto.setUsername(comment.getUsername());
        commentUpdateDto.setNewsId(comment.getNewsId());

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token.accessToken())
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteNewsShouldReturnStatus204() throws Exception {
        SignInDto signInDto = new SignInDto("username123", "username123");
        Long userId = 2L;
        var token = authClient.signIn(signInDto);

        CommentCreateDto commentCreateDto = new CommentCreateDto("text", "username123", 1L);
        var comment = commentService.createComment(commentCreateDto, token.accessToken());
        Long newsId = comment.getId();

        mockMvc.perform(delete("/api/comments/" + newsId + "/" + userId)
                        .header("Authorization", token.accessToken()))
                .andExpect(status().isNoContent());
    }
}

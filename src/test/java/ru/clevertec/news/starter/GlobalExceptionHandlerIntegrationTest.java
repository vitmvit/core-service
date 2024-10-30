package ru.clevertec.news.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.AuthTestBuilder;
import ru.clevertec.news.util.NewsTestBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.clevertec.news.constant.Constant.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @MockBean
    private AuthClient authClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        var id = 1L;
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        var jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        when(authClient.signUp(signUpDto)).thenReturn(jwtDto);
        when(newsService.getByIdWithComments(OFFSET, LIMIT, id)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/news/" + id)
                        .header(AUTHORIZATION_HEADER, jwtDto.accessToken()))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void testHandleNoAccessException() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        when(newsService.update(newsUpdateDto)).thenThrow(new NoAccessError());

        mockMvc.perform(get("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsUpdateDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
    }

    @Test
    public void testHandleInvalidJwtException() throws Exception {
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        when(authClient.signIn(AuthTestBuilder.builder().build().buildSignInDto())).thenThrow(new InvalidJwtException(USERNAME_NOT_EXIST));

        mockMvc.perform(post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }
}
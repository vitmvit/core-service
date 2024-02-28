//package ru.clevertec.news.starter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import ru.clevertec.news.config.PostgresSqlContainerInitializer;
//import ru.clevertec.news.config.TokenProvider;
//import ru.clevertec.news.dto.auth.SignInDto;
//import ru.clevertec.news.dto.update.NewsUpdateDto;
//import ru.clevertec.news.exception.EmptyListException;
//import ru.clevertec.news.exception.EntityNotFoundException;
//import ru.clevertec.news.exception.InvalidJwtException;
//import ru.clevertec.news.exception.NoAccessError;
//import ru.clevertec.news.service.AuthService;
//import ru.clevertec.news.service.NewsService;
//import ru.clevertec.news.util.AuthTestBuilder;
//import ru.clevertec.news.util.NewsTestBuilder;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static ru.clevertec.news.constant.Constant.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class GlobalExceptionHandlerIntegrationTest extends PostgresSqlContainerInitializer {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private NewsService newsService;
//
//    @MockBean
//    private AuthService authService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TokenProvider tokenProvider;
//
//    @Test
//    public void testHandleEntityNotFoundException() throws Exception {
//        Long id = 1L;
//
//        when(newsService.findNewsById(id)).thenThrow(new EntityNotFoundException());
//
//        mockMvc.perform(get("/api/news/" + id))
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void testHandleEmptyListException() throws Exception {
//        String fragment = "k";
//
//        when(newsService.searchNewsByText(OFFSET, LIMIT, fragment)).thenThrow(new EmptyListException());
//
//        mockMvc.perform(get("/api/news/search/text/" + fragment))
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void testHandleNoAccessException() throws Exception {
//        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
//
//        when(newsService.updateNews(newsUpdateDto)).thenThrow(new NoAccessError(NO_ACCESS_ERROR));
//
//        mockMvc.perform(get("/api/news")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newsUpdateDto)))
//                .andExpect(MvcResult::getResolvedException).getClass().equals(NoAccessError.class);
//    }
//
//    @Test
//    public void testHandleInvalidJwtException() throws Exception {
//        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();
//
//        when(authService.signIn(AuthTestBuilder.builder().build().buildSignInDto())).thenThrow(new InvalidJwtException(USERNAME_NOT_EXIST));
//
//        mockMvc.perform(post("/api/signIn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(signInDto)))
//                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
//    }
//}

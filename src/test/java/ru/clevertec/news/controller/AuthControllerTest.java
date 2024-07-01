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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthClient authClient;

    @Test
    void signUpSuccess() throws Exception {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        JwtDto jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        when(authClient.signUp(signUpDto)).thenReturn(jwtDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signUpShouldReturnInvalidJwtException() throws Exception {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        String signUpDtoJson = objectMapper.writeValueAsString(signUpDto);

        when(authClient.signUp(signUpDto)).thenThrow(InvalidJwtException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    public void signInSuccess() throws Exception {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();
        JwtDto jwtDto = AuthTestBuilder.builder().build().buildJwtDto();

        when(authClient.signIn(signInDto)).thenReturn(jwtDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signInSuccessShouldReturnInvalidJwtException() throws Exception {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        when(authClient.signIn(signInDto)).thenThrow(InvalidJwtException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }
}

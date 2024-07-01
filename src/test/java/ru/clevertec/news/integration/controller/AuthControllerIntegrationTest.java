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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.exception.InvalidJwtException;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    void signUpSuccess() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.USER);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    public void signUpShouldReturnInvalidJwtException() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.USER);
        String signUpDtoJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    @Sql(scripts = "classpath:sql/delete-test-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD)
    public void signInSuccess() throws Exception {
        SignUpDto signUpDto = new SignUpDto("login1", "login1", RoleName.USER);
        SignInDto signInDto = new SignInDto("login1", "login1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signInSuccessShouldReturnInvalidJwtException() throws Exception {
        SignInDto signInDto = new SignInDto("login1", "login1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }
}


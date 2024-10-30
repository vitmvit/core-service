package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.entity.User;

@Builder(setterPrefix = "with")
public class AuthTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String login = "TestUser";

    @Builder.Default
    private String password = "TestUser";

    @Builder.Default
    private RoleName role = RoleName.ADMIN;

    public SignUpDto buildSignUpDto() {
        return new SignUpDto(login, password, role);
    }

    public SignInDto buildSignInDto() {
        return new SignInDto(login, password);
    }

    public JwtDto buildJwtDto() {
        return new JwtDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0VXNlciIsInVzZXJuYW1lIjoiVGVzdFVzZXIiLCJyb2xlIjoiQURNSU4iLCJleHAiOjE4MzAzMjMxNTR9.VNSwmhCl4Xw73TrYlSwbahbdW2u9opdS1A-LoGTIzCw");
    }

    public Long getId() {
        return id;
    }

    public User buildUser() {
        return new User(id, login, password, role);
    }
}
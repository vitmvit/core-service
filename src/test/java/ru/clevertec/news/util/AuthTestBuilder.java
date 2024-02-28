package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.model.User;

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

    public User buildUser() {
        return new User(login, password, role);
    }

    public JwtDto buildJwtDto() {
        return new JwtDto("login");
    }
}

package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.service.AuthService;

import static ru.clevertec.news.constant.Constant.AUTHORIZATION_HEADER;

@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto signUp(@RequestBody @Valid SignUpDto dto) {
        return authService.signUp(dto);
    }

    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto signIn(@RequestBody @Valid SignInDto dto) {
        return authService.signIn(dto);
    }

    @PostMapping("/check")
    public boolean check(@RequestHeader(AUTHORIZATION_HEADER) String auth) {
        return authService.check(auth);
    }
}
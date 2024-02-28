package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.feign.AuthClient;

import static ru.clevertec.news.constant.Constant.USERNAME_IS_EXIST;
import static ru.clevertec.news.constant.Constant.USERNAME_NOT_EXIST;

/**
 * Контроллер аутентификации.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthClient authClient;

    /**
     * Регистрация нового пользователя.
     *
     * @param dto данные регистрации пользователя
     * @return JWT токен
     */
    @PostMapping("/signUp")
    public ResponseEntity<JwtDto> signUp(@RequestBody @Valid SignUpDto dto) {
        try {
            return ResponseEntity.ok(authClient.signUp(dto));
        } catch (Exception e) {
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
    }

    /**
     * Аутентификация пользователя.
     *
     * @param dto данные аутентификации пользователя
     * @return JWT токен
     */
    @PostMapping("/signIn")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto dto) {
        try {
            return ResponseEntity.ok(authClient.signIn(dto));
        } catch (Exception e) {
            throw new InvalidJwtException(USERNAME_NOT_EXIST);
        }
    }
}

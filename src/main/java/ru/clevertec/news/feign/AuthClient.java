package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;

/**
 * Клиент Feign для взаимодействия с сервисом аутентификации.
 */
@FeignClient(contextId = "authClient", value = "authService", url = "http://localhost:8081/api/auth")
public interface AuthClient {

    /**
     * Регистрация нового пользователя.
     *
     * @param dto данные регистрации пользователя
     * @return JWT токен
     */
    @PostMapping("/signUp")
    JwtDto signUp(@RequestBody SignUpDto dto);

    /**
     * Аутентификация пользователя.
     *
     * @param dto данные аутентификации пользователя
     * @return JWT токен
     */
    @PostMapping("/signIn")
    JwtDto signIn(@RequestBody SignInDto dto);
}

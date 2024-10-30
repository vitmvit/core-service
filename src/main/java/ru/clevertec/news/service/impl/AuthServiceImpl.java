package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.exception.OperationException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.AuthService;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthClient authClient;

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param dto Объект данных для регистрации пользователя, содержащий необходимую информацию.
     * @return JwtDto Объект с информацией о JWT (JSON Web Token) после успешной регистрации.
     * @throws OperationException В случае ошибки при выполнении операции регистрации.
     */
    @Override
    public JwtDto signUp(SignUpDto dto) {
        try {
            log.info("AuthService: singUp");
            return authClient.signUp(dto);
        } catch (Exception e) {
            log.error("AuthService: singUp error - " + e.getMessage());
            throw new OperationException("singUp error - " + e.getMessage());
        }
    }

    /**
     * Метод для входа пользователя.
     *
     * @param dto Объект данных для входа пользователя, содержащий необходимые учетные данные.
     * @return JwtDto Объект с информацией о JWT (JSON Web Token) после успешного входа.
     * @throws OperationException В случае ошибки при выполнении операции входа.
     */
    @Override
    public JwtDto signIn(SignInDto dto) {
        try {
            log.info("AuthService: singIn");
            return authClient.signIn(dto);
        } catch (Exception e) {
            log.error("AuthService: singIn error - " + e.getMessage());
            throw new OperationException("singIn error - " + e.getMessage());
        }
    }

    /**
     * Метод для проверки действительности токена авторизации.
     *
     * @param auth Строка, представляющая токен авторизации, который необходимо проверить.
     * @return boolean Возвращает true, если токен действителен, иначе false.
     * @throws OperationException В случае ошибки при выполнении проверки токена.
     */
    @Override
    public boolean check(String auth) {
        try {
            log.info("AuthService: check");
            return authClient.check(auth);
        } catch (Exception e) {
            log.error("AuthService: check error - " + e.getMessage());
            throw new OperationException("check error - " + e.getMessage());
        }
    }
}
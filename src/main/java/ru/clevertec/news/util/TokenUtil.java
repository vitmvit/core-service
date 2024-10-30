package ru.clevertec.news.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.news.entity.TokenPayload;
import ru.clevertec.news.exception.OperationException;

import java.util.Base64;

import static ru.clevertec.news.constant.Constant.PARSE_EXCEPTION;

@Component
public class TokenUtil {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Получает имя пользователя из токена.
     *
     * @param token строка, представляющая JWT токен
     * @return имя пользователя из токена
     * @throws RuntimeException при возникновении ошибки при разборе JSON
     */
    public String getUsername(String token) {
        try {
            var chinks = token.split("\\.");
            var decoder = Base64.getUrlDecoder();
            var payload = new String(decoder.decode(chinks[1]));
            var tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
            return tokenPayload.getUsername();
        } catch (Exception e) {
            throw new OperationException(PARSE_EXCEPTION);
        }
    }
}
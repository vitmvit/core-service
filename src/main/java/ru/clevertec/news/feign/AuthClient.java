package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;

import static ru.clevertec.news.constant.Constant.AUTHORIZATION_HEADER;

@FeignClient(contextId = "authClient", value = "${feign.auth-service.value}", url = "${feign.auth-service.url}")
public interface AuthClient {

    @PostMapping("/signUp")
    JwtDto signUp(@RequestBody SignUpDto dto);

    @PostMapping("/signIn")
    JwtDto signIn(@RequestBody SignInDto dto);

    @PostMapping("/check")
    boolean check(@RequestHeader(AUTHORIZATION_HEADER) String auth);
}
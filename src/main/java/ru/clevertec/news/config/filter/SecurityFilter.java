package ru.clevertec.news.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.news.config.service.UserDetailsServiceImpl;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.util.TokenUtil;

import static ru.clevertec.news.constant.Constant.*;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private AuthClient authClient;
    private TokenUtil tokenUtil;
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Метод, выполняющий перехват запросов и проверку токена.
     *
     * @param request     объект HttpServletRequest, представляющий HTTP запрос
     * @param response    объект HttpServletResponse, представляющий HTTP ответ
     * @param filterChain объект FilterChain, представляющий цепочку фильтров
     * @throws InvalidJwtException если токен недействителен
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            if (request.getRequestURI().contains("/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            var token = this.recoverToken(request);
            var login = tokenUtil.getUsername(token);
            var user = userDetailsService.loadUserByUsername(login);
            var authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (Boolean.FALSE.equals(authClient.check(token))) {
                throw new InvalidJwtException(INVALID_TOKEN_ERROR);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new InvalidJwtException(INVALID_TOKEN_ERROR);
        }
    }

    /**
     * Метод для извлечения токена из HTTP запроса.
     *
     * @param request объект HttpServletRequest, представляющий HTTP запрос
     * @return строковое значение токена или {@code null}, если токен отсутствует
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authHeader == null ? null : authHeader.replace(BEARER_PREFIX, "");
    }
}
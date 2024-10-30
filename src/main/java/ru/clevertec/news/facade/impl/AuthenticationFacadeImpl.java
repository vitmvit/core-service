package ru.clevertec.news.facade.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.clevertec.news.entity.User;
import ru.clevertec.news.facade.AuthenticationFacade;

/**
 * Фасад для получения информации об аутентифицированном пользователе.
 *
 * <p>Данный класс предоставляет методы для получения идентификатора текущего пользователя и его роли из контекста безопасности.</p>
 */
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    /**
     * Получить идентификатор текущего аутентифицированного пользователя.
     *
     * @return идентификатор текущего пользователя
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((User) authentication.getPrincipal()).getId();
        }
        throw new IllegalStateException("User not authenticated");
    }

    /**
     * Получить имя текущего аутентифицированного пользователя.
     *
     * @return имя текущего пользователя
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((User) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }

    /**
     * Получить роль текущего аутентифицированного пользователя.
     *
     * @return роль текущего пользователя или {@code null}, если роль не установлена
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public String getCurrentUserRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        throw new IllegalStateException("User not authenticated");
    }
}
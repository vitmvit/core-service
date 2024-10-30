package ru.clevertec.news.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.news.config.filter.SecurityFilter;
import ru.clevertec.news.config.service.UserDetailsServiceImpl;
import ru.clevertec.news.dto.constant.RoleName;

/**
 * Конфигурация безопасности
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Настройка игнорируемых запросов для безопасности.
     *
     * @return WebSecurityCustomizer для настройки игнорируемых запросов
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/api/auth/**",
                "/swagger-ui/**",
                "/api/doc/**",
                "/v3/api-docs/**"
        );
    }

    /**
     * Создает {@link SecurityFilterChain} для настройки правил доступа к конечным точкам API.
     * Этот метод определяет правила доступа и добавляет кастомный фильтр безопасности.
     *
     * @param httpSecurity объект {@link HttpSecurity} для настройки безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникают проблемы с настройкой безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole(RoleName.USER.name(), RoleName.JOURNALIST.name(), RoleName.SUBSCRIBER.name())
                        .requestMatchers(HttpMethod.POST, "/api/news/**").hasAnyRole(RoleName.JOURNALIST.name(), RoleName.ADMIN.getRole())
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").hasAnyRole(RoleName.SUBSCRIBER.name(), RoleName.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PUT, "/api/news/**").hasAnyRole(RoleName.JOURNALIST.name(), RoleName.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").hasAnyRole(RoleName.SUBSCRIBER.name(), RoleName.ADMIN.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/api/news/**").hasAnyRole(RoleName.JOURNALIST.name(), RoleName.ADMIN.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole(RoleName.SUBSCRIBER.name(), RoleName.ADMIN.getRole())
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Создает провайдер аутентификации, который использует
     * {@link DaoAuthenticationProvider}. Провайдер настраивается
     * с использованием {@link UserDetailsServiceImpl} и
     * {@link PasswordEncoder}, что позволяет аутентифицировать
     * пользователей на основе их учетных данных.
     *
     * @return Настроенный {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Настраивает менеджер аутентификации.
     *
     * @param config Конфигурация аутентификации.
     * @return Настроенный {@link AuthenticationManager}.
     * @throws Exception Если не удается получить менеджер аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создание кодировщика пароля.
     *
     * @return кодировщик пароля
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
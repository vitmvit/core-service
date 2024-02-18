package ru.clevertec.news.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.news.config.auth.TokenProvider;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.model.User;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.AuthService;

import static ru.clevertec.news.constant.Constant.USERNAME_IS_EXIST;
import static ru.clevertec.news.constant.Constant.USERNAME_NOT_EXIST;

/**
 * Реализация сервиса аутентификации
 */
@Service
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           TokenProvider tokenProvider,
                           @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Метод для поиска пользователя по его логину
     *
     * @param username логин пользователя
     * @return объект UserDetails, представляющий пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    /**
     * Метод для регистрации нового пользователя
     *
     * @param dto объект SignUpDto, содержащий данные для регистрации
     * @return объект JwtDto, содержащий JWT-токен
     */
    public JwtDto signUp(SignUpDto dto) {
        if (userRepository.existsByLogin(dto.login())) {
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User newUser = new User(dto.login(), encryptedPassword, dto.role());
        userRepository.save(newUser);
        return buildJwt(dto.login(), dto.password());
    }

    /**
     * Метод для аутентификации пользователя
     *
     * @param dto объект SignInDto, содержащий данные для аутентификации
     * @return объект JwtDto, содержащий JWT-токен
     */
    public JwtDto signIn(SignInDto dto) {
        if (!userRepository.existsByLogin(dto.login())) {
            throw new InvalidJwtException(USERNAME_NOT_EXIST);
        }
        return buildJwt(dto.login(), dto.password());
    }

    /**
     * Метод для создания и возвращения JWT-токена
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект JwtDto, содержащий JWT-токен
     */
    private JwtDto buildJwt(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return new JwtDto(accessToken);
    }
}

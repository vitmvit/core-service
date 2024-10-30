package ru.clevertec.news.config.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.clevertec.news.entity.User;
import ru.clevertec.news.exception.OperationException;
import ru.clevertec.news.repository.UserRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя по имени пользователя.
     * <p>
     * Этот метод проверяет, существует ли пользователь с указанным именем в
     * репозитории пользователей. Если пользователь найден, возвращается
     * {@link UserDetails} пользователя. Если пользователь не найден, выбрасывается
     * {@link UsernameNotFoundException}.
     *
     * @param username имя пользователя, данные которого необходимо получить
     * @return {@link UserDetails} запрашиваемого пользователя
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByLogin(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new OperationException("User not found");
        }
    }
}
package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.model.User;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.impl.UserServiceImpl;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByLoginShouldReturnExpectedUserWhenFound() {
        String username = "username";
        User expected = AuthTestBuilder.builder().build().buildUser();

        when(userRepository.findByLogin(username)).thenReturn(expected);

        User actual = userService.findByLogin(username);
        assertThat(actual)
                .hasFieldOrPropertyWithValue(User.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(User.Fields.login, expected.getLogin())
                .hasFieldOrPropertyWithValue(User.Fields.password, expected.getPassword());
    }
}

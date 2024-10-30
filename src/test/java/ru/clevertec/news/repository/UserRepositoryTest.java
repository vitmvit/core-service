package ru.clevertec.news.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends PostgresSqlContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLoginShouldReturnExpectedUserDetails() {
        var user = AuthTestBuilder.builder().build().buildUser();
        userRepository.save(user);

        var actual = userRepository.findByLogin(user.getLogin());

        assertNotNull(actual);
        assertEquals(user.getLogin(), actual.get().getLogin());
    }
}
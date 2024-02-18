package ru.clevertec.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.news.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login);

    boolean existsByLogin(String login);
}

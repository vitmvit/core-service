package ru.clevertec.news.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails findByLogin(String login);
}

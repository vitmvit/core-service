package ru.clevertec.news.facade;

public interface AuthenticationFacade {

    Long getCurrentUserId();

    String getCurrentUsername();

    String getCurrentUserRole();
}
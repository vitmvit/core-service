package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.NewsClient;
import ru.clevertec.news.service.NewsService;

/**
 * Реализация сервиса новостей
 */
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsClient newsClient;

    /**
     * Найти все новости с пагинацией
     *
     * @param offset смещение
     * @param limit  лимит
     * @return страница новостей
     */
    @Override
    public Page<NewsDto> findAllNews(Integer offset, Integer limit) {
        try {
            return newsClient.getAllNews(offset, limit);
        } catch (Exception e) {
            throw new EmptyListException();
        }
    }

    /**
     * Найти новость по идентификатору
     *
     * @param id идентификатор новости
     * @return новость
     */
    @Override
    public NewsDto findNewsById(Long id) {
        try {
            return newsClient.getNewsById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Найти новость по идентификатору с комментариями и пагинацией
     *
     * @param offset смещение
     * @param limit  лимит
     * @param id     идентификатор новости
     * @return новость с комментариями
     */
    @Override
    public NewsDto findNewsByIdWithComments(Integer offset, Integer limit, Long id) {
        try {
            return newsClient.getByIdWithComments(offset, limit, id);
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Поиск новостей по фрагменту текста с пагинацией
     *
     * @param offset   смещение
     * @param limit    лимит
     * @param fragment текстовый фрагмент для поиска
     * @return страница новостей
     */
    @Override
    public Page<NewsDto> searchNewsByText(Integer offset, Integer limit, String fragment) {
        try {
            return newsClient.searchNewsByText(offset, limit, fragment);
        } catch (Exception e) {
            throw new EmptyListException();
        }
    }

    /**
     * Поиск новостей по фрагменту заголовка с пагинацией
     *
     * @param offset   смещение
     * @param limit    лимит
     * @param fragment текстовый фрагмент для поиска
     * @return страница новостей
     */
    @Override
    public Page<NewsDto> searchNewsByTitle(Integer offset, Integer limit, String fragment) {
        try {
            return newsClient.searchNewsByText(offset, limit, fragment);
        } catch (Exception e) {
            throw new EmptyListException();
        }
    }

    /**
     * Создать новость
     *
     * @param newsCreateDto DTO для создания новости
     * @param auth          данные аутентификации
     * @return созданная новость
     */
    @Override
    public NewsDto createNews(NewsCreateDto newsCreateDto, String auth) {
        try {
            return newsClient.createNews(newsCreateDto, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }

    /**
     * Обновить новость
     *
     * @param newsUpdateDto DTO для обновления новости
     * @param auth          данные аутентификации
     * @return обновленная новость
     */
    @Override
    public NewsDto updateNews(NewsUpdateDto newsUpdateDto, String auth) {
        try {
            newsClient.getNewsById(newsUpdateDto.getId());
        } catch (Exception e) {
            throw new EmptyListException();
        }
        try {
            return newsClient.updateNews(newsUpdateDto, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }

    /**
     * Удалить новость
     *
     * @param id     идентификатор новости
     * @param userId идентификатор пользователя
     * @param auth   данные аутентификации
     */
    @Override
    public void deleteNews(Long id, Long userId, String auth) {
        try {
            newsClient.deleteNews(id, userId, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }
}

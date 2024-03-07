package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

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
            logger.info("NewsService: find all news");
            return newsClient.getAllNews(offset, limit);
        } catch (Exception e) {
            logger.error("NewsService: Empty list error");
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
            logger.info("NewsService: find news by id: " + id);
            return newsClient.getNewsById(id);
        } catch (Exception e) {
            logger.error("NewsService: Entity not found error");
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
            logger.info("NewsService: find news with comments by id: " + id);
            return newsClient.getByIdWithComments(offset, limit, id);
        } catch (Exception e) {
            logger.error("NewsService: Entity not found error");
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
            logger.info("NewsService: search news by text fragment: " + fragment);
            return newsClient.searchNewsByText(offset, limit, fragment);
        } catch (Exception e) {
            logger.error("NewsService: Empty list error");
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
            logger.info("NewsService: search news by title fragment: " + fragment);
            return newsClient.searchNewsByText(offset, limit, fragment);
        } catch (Exception e) {
            logger.error("NewsService: Empty list error");
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
            logger.debug("NewsService: create news: " + newsCreateDto);
            return newsClient.createNews(newsCreateDto, auth);
        } catch (Exception e) {
            logger.error("NewsService: No access error");
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
            logger.debug("NewsService: update news (get by id): " + newsUpdateDto.getId());
            newsClient.getNewsById(newsUpdateDto.getId());
        } catch (Exception e) {
            throw new EmptyListException();
        }
        try {
            logger.debug("NewsService: update news: " + newsUpdateDto);
            return newsClient.updateNews(newsUpdateDto, auth);
        } catch (Exception e) {
            logger.error("NewsService: No access error");
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
            logger.debug("NewsService: delete news by id: " + id);
            newsClient.deleteNews(id, userId, auth);
        } catch (Exception e) {
            logger.error("NewsService: No access error");
            throw new NoAccessError();
        }
    }
}

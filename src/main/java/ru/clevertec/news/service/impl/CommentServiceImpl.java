package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.CommentService;

/**
 * Реализация сервиса комментариев
 */
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentClient commentClient;
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    /**
     * Найти комментарий по его идентификатору
     *
     * @param id идентификатор комментария
     * @return найденный комментарий
     * @throws EntityNotFoundException если комментарий не найден
     */
    @Override
    public CommentDto findCommentById(Long id) {
        try {
            logger.info("Service: find comment by id: " + id);
            return commentClient.getCommentById(id);
        } catch (Exception e) {
            logger.error("Service: Entity not found error");
            throw new EntityNotFoundException();
        }
    }

    /**
     * Поиск комментариев по фрагменту текста комментария
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment фрагменту текста комментария для поиска
     * @return страница комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchCommentsByText(Integer offset, Integer limit, String fragment) {
        try {
            logger.info("Service: search comment by text fragment: " + fragment);
            return commentClient.searchCommentsByText(offset, limit, fragment);
        } catch (Exception e) {
            logger.error("Service: Empty list error");
            throw new EmptyListException();
        }
    }

    /**
     * Поиск комментариев по фрагменту имени пользователя
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment фрагмент имени пользователя для поиска комментариев
     * @return страница комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchCommentsByUsername(Integer offset, Integer limit, String fragment) {
        try {
            logger.info("Service: search comment by username fragment: " + fragment);
            return commentClient.searchCommentByUsername(offset, limit, fragment);
        } catch (Exception e) {
            logger.error("Service: Empty list error");
            throw new EmptyListException();
        }
    }

    /**
     * Создать новый комментарий
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             заголовок Authorization
     * @return созданный комментарий
     * @throws NoAccessError если нет доступа к созданию комментария
     */
    @Override
    public CommentDto createComment(CommentCreateDto commentCreateDto, String auth) {
        try {
            logger.debug("Service: create comment: " + commentCreateDto);
            return commentClient.createComment(commentCreateDto, auth);
        } catch (Exception e) {
            logger.error("Service: No access error");
            throw new NoAccessError();
        }
    }

    /**
     * Обновить комментарий
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             заголовок Authorization
     * @return обновленный комментарий
     * @throws NoAccessError если нет доступа к обновлению комментария
     */
    @Override
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, String auth) {
        try {
            logger.debug("Service: update comment: " + commentUpdateDto);
            return commentClient.updateComment(commentUpdateDto, auth);
        } catch (Exception e) {
            logger.error("Service: No access error");
            throw new NoAccessError();
        }
    }

    /**
     * Удалить комментарий
     *
     * @param id     идентификатор комментария
     * @param userId идентификатор пользователя
     * @param auth   заголовок Authorization
     * @throws NoAccessError если нет доступа к удалению комментария
     */
    @Override
    public void deleteComment(Long id, Long userId, String auth) {
        try {
            logger.debug("Service: delete comment by id: " + id);
            commentClient.deleteComment(id, userId, auth);
        } catch (Exception e) {
            logger.error("Service: No access error");
            throw new NoAccessError();
        }
    }
}

package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
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
            return commentClient.getCommentById(id);
        } catch (Exception e) {
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
            return commentClient.searchCommentsByText(offset, limit, fragment);
        } catch (Exception e) {
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
            return commentClient.searchCommentByUsername(offset, limit, fragment);
        } catch (Exception e) {
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
            return commentClient.createComment(commentCreateDto, auth);
        } catch (Exception e) {
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
            return commentClient.updateComment(commentUpdateDto, auth);
        } catch (Exception e) {
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
            commentClient.deleteComment(id, userId, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }
}

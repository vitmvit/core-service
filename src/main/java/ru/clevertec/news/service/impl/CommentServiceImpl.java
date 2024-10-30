package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.exception.OperationException;
import ru.clevertec.news.facade.AuthenticationFacade;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.ADMIN_ROLE;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentClient commentClient;
    private final AuthenticationFacade authenticationFacade;

    /**
     * Получить комментарий по его идентификатору.
     *
     * @param id идентификатор комментария
     * @return объект {@link CommentDto} с найденным комментарием
     * @throws OperationException если произошла ошибка при получении комментария
     */
    @Override
    public CommentDto getById(Long id) {
        try {
            log.info("CommentService: find comment by id: " + id);
            return commentClient.getById(id);
        } catch (Exception e) {
            log.error("CommentService: Get by id error - " + e.getMessage());
            throw new OperationException("Get by id error - " + e.getMessage());
        }
    }


    /**
     * Получить все комментарии с возможностью фильтрации и пагинации.
     *
     * @param pageNumber номер страницы для пагинации
     * @param pageSize   количество комментариев на странице
     * @param username   имя пользователя для фильтрации комментариев
     * @param text       текст для фильтрации комментариев
     * @return объект {@link PageContentDto} с содержимым комментариев
     * @throws OperationException если произошла ошибка при получении комментариев
     */
    @Override
    public PageContentDto<CommentDto> getAll(int pageNumber, int pageSize, String username, String text) {
        try {
            log.info("CommentService: get all");
            return commentClient.getAll(pageNumber, pageSize, username, text);
        } catch (Exception e) {
            log.error("CommentService: Get all comments error - " + e.getMessage());
            throw new OperationException("Get all comments error - " + e.getMessage());
        }
    }

    /**
     * Получить комментарии по идентификатору новости.
     *
     * @param pageNumber номер страницы для пагинации
     * @param pageSize   количество комментариев на странице
     * @param id         идентификатор новости
     * @return объект {@link PageContentDto} с комментариями к указанной новости
     * @throws OperationException если произошла ошибка при получении комментариев
     */
    @Override
    public PageContentDto<CommentDto> getByNewsId(int pageNumber, int pageSize, Long id) {
        try {
            log.info("CommentService: get by news id - " + id);
            return commentClient.getByNewsId(pageNumber, pageSize, id);
        } catch (Exception e) {
            log.error("CommentService: Get by news id error - " + e.getMessage());
            throw new OperationException("Get by news id error - " + e.getMessage());
        }
    }

    /**
     * Создать новый комментарий.
     * Метод проверяет права пользователя на удаление комментария.
     * Пользователь может создать комментарий, если он является его создателем или если его роль - администратор.
     *
     * @param dto объект данных для создания комментария
     * @return объект {@link CommentDto} с созданным комментарием
     * @throws NoAccessError      если пользователь не имеет прав для создания комментария
     * @throws OperationException если произошла ошибка при создании комментария
     */
    @Override
    public CommentDto create(CommentCreateDto dto) {
        try {
            log.info("CommentService: create news: " + dto);
            var currentUsername = authenticationFacade.getCurrentUsername();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("CommentService: current role " + currentUserRole + "\n current id - " + currentUsername);
            if (!currentUserRole.equals(ADMIN_ROLE) && !dto.getUsername().equals(currentUsername)) {
                log.error("CommentService: No access error");
                throw new NoAccessError();
            }
            return commentClient.create(dto);
        } catch (Exception e) {
            log.error("CommentService: Create comment error - " + e.getMessage());
            throw new OperationException("Create comment error - " + e.getMessage());
        }
    }

    /**
     * Обновить существующий комментарий.
     * Метод проверяет права пользователя на удаление комментария.
     * Пользователь может обновить комментарий, если он является его создателем или если его роль - администратор.
     *
     * @param dto объект данных для обновления комментария
     * @return объект {@link CommentDto} с обновленным комментарием
     * @throws NoAccessError      если пользователь не имеет прав для обновления комментария
     * @throws OperationException если произошла ошибка при обновлении комментария
     */
    @Override
    public CommentDto update(CommentUpdateDto dto) {
        try {
            log.debug("CommentService: update comment: " + dto);
            var currentUsername = authenticationFacade.getCurrentUsername();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("CommentService: current role " + currentUserRole + "\n current id - " + currentUsername);
            if (!currentUserRole.equals(ADMIN_ROLE) && !dto.getUsername().equals(currentUsername)) {
                log.error("CommentService: No access error");
                throw new NoAccessError();
            }
            return commentClient.update(dto);
        } catch (Exception e) {
            log.error("CommentService: Update comment error - " + e.getMessage());
            throw new OperationException("Update comment error - " + e.getMessage());
        }
    }

    /**
     * Удалить комментарий по его идентификатору.
     * Метод проверяет права пользователя на удаление комментария.
     * Пользователь может удалить комментарий, если он является его создателем или если его роль - администратор.
     *
     * @param id идентификатор комментария, который требуется удалить
     * @throws NoAccessError      если пользователь не имеет прав для удаления комментария
     * @throws OperationException если произошла ошибка при удалении комментария
     */
    @Override
    public void delete(Long id) {
        try {
            log.debug("CommentService: delete comment by id: " + id);
            var currentUsername = authenticationFacade.getCurrentUsername();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("CommentService: current role " + currentUserRole + "\n current user name - " + currentUsername);
            var newsDto = commentClient.getById(id);
            if (!newsDto.getUsername().equals(currentUsername) && !currentUserRole.equals(ADMIN_ROLE)) {
                log.error("CommentService: No access error");
                throw new NoAccessError();
            }
            commentClient.delete(id);
        } catch (Exception e) {
            log.error("CommentService: Delete comment error - " + e.getMessage());
            throw new OperationException("Delete comment error - " + e.getMessage());
        }
    }
}
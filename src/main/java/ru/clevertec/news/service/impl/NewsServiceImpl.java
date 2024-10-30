package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.exception.OperationException;
import ru.clevertec.news.facade.AuthenticationFacade;
import ru.clevertec.news.feign.NewsClient;
import ru.clevertec.news.service.NewsService;

import static ru.clevertec.news.constant.Constant.ADMIN_ROLE;

@Slf4j
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsClient newsClient;
    private final AuthenticationFacade authenticationFacade;

    /**
     * Получить все новости с возможностью фильтрации и пагинации.
     *
     * @param pageNumber номер страницы для пагинации
     * @param pageSize   количество новостей на странице
     * @param title      заголовок для фильтрации новостей
     * @param text       текст для фильтрации новостей
     * @return объект {@link PageContentDto} с содержимым новостей
     * @throws OperationException если произошла ошибка при получении новостей
     */
    @Override
    public PageContentDto<NewsDto> getAll(int pageNumber, int pageSize, String title, String text) {
        try {
            log.info("NewsService: find all news");
            return newsClient.getAll(pageNumber, pageSize, title, text);
        } catch (Exception e) {
            log.error("NewsService: Get all error - " + e.getMessage());
            throw new OperationException("Get all error - " + e.getMessage());
        }
    }

    /**
     * Получить новость по её идентификатору.
     *
     * @param id идентификатор новости
     * @return объект {@link NewsDto} с найденной новостью
     * @throws OperationException если произошла ошибка при получении новости
     */
    @Override
    public NewsDto getNewsById(Long id) {
        try {
            log.info("NewsService: find news by id: " + id);
            return newsClient.getNewsById(id);
        } catch (Exception e) {
            log.error("NewsService: Get news by id error - " + e.getMessage());
            throw new OperationException("Get news by id error - " + e.getMessage());
        }
    }

    /**
     * Получить новость с комментариями по её идентификатору.
     *
     * @param pageNumber номер страницы для пагинации
     * @param pageSize   количество комментариев на странице
     * @param id         идентификатор новости
     * @return объект {@link NewsDto} с найденной новостью и её комментариями
     * @throws OperationException если произошла ошибка при получении новости с комментариями
     */
    @Override
    public NewsDto getByIdWithComments(Integer pageNumber, Integer pageSize, Long id) {
        try {
            log.info("NewsService: find news with comments by id: " + id);
            return newsClient.getByIdWithComments(pageNumber, pageSize, id);
        } catch (Exception e) {
            log.error("NewsService: Get news by id error - " + e.getMessage());
            throw new OperationException("Get news by id error - " + e.getMessage());
        }
    }

    /**
     * Создать новую новость.
     * Метод проверяет права пользователя на удаление новости.
     * Пользователь может создать новость, если он является её создателем или если его роль - администратор.
     *
     * @param dto объект данных для создания новости
     * @return объект {@link NewsDto} с созданной новостью
     * @throws NoAccessError      если пользователь не имеет прав для создания новости
     * @throws OperationException если произошла ошибка при создании новости
     */
    @Override
    public NewsDto create(NewsCreateDto dto) {
        try {
            log.info("NewsService: create news: " + dto);
            var currentUserId = authenticationFacade.getCurrentUserId();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("NewsService: current role " + currentUserRole + "\n current id - " + currentUserId);
            if (!currentUserRole.equals(ADMIN_ROLE) && !dto.getUserId().equals(currentUserId)) {
                log.error("NewsService: No access error");
                throw new NoAccessError();
            }
            return newsClient.create(dto);
        } catch (Exception e) {
            log.error("NewsService: Create news error - " + e.getMessage());
            throw new OperationException("Create news error - " + e.getMessage());
        }
    }

    /**
     * Обновить существующую новость.
     * Метод проверяет права пользователя на удаление новости.
     * Пользователь может обновить новость, если он является её создателем или если его роль - администратор.
     *
     * @param newsUpdateDto объект данных для обновления новости
     * @return объект {@link NewsDto} с обновленной новостью
     * @throws NoAccessError      если пользователь не имеет прав для обновления новости
     * @throws OperationException если произошла ошибка при обновлении новости
     */
    @Override
    public NewsDto update(NewsUpdateDto newsUpdateDto) {
        try {
            log.debug("NewsService: update news: " + newsUpdateDto);
            var currentUserId = authenticationFacade.getCurrentUserId();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("NewsService: current role " + currentUserRole + "\n current id - " + currentUserId);
            if (!currentUserRole.equals(ADMIN_ROLE) && !newsUpdateDto.getUserId().equals(currentUserId)) {
                log.error("NewsService: No access error");
                throw new NoAccessError();
            }
            return newsClient.update(newsUpdateDto);
        } catch (Exception e) {
            log.error("NewsService: Update news error - " + e.getMessage());
            throw new OperationException("Update news error - " + e.getMessage());
        }
    }

    /**
     * Удалить новость по её идентификатору.
     * Метод проверяет права пользователя на удаление новости.
     * Пользователь может удалить новость, если он является её создателем или если его роль - администратор.
     *
     * @param id идентификатор новости, которую требуется удалить
     * @throws NoAccessError      если пользователь не имеет прав для удаления новости
     * @throws OperationException если произошла ошибка при удалении новости
     */
    @Override
    public void delete(Long id) {
        try {
            log.debug("NewsService: delete news by id: " + id);
            var currentUserId = authenticationFacade.getCurrentUserId();
            var currentUserRole = authenticationFacade.getCurrentUserRole();
            log.info("NewsService: current role " + currentUserRole + "\n current id - " + currentUserId);
            var newsDto = newsClient.getNewsById(id);
            if (!newsDto.getUserId().equals(currentUserId) && !currentUserRole.equals(ADMIN_ROLE)) {
                log.error("NewsService: No access error");
                throw new NoAccessError();
            }
            newsClient.delete(id);
        } catch (Exception e) {
            log.error("NewsService: Delete news error - " + e.getMessage());
            throw new OperationException("Delete news error - " + e.getMessage());
        }
    }
}
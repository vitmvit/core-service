package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Клиент Feign для взаимодействия с сервисом комментариев через сервис новостей
 */
@FeignClient(contextId = "newsClient", value = "newsService", url = "http://localhost:8082/api/comments")
public interface CommentClient {

    /**
     * Получить комментарий новости по его идентификатору
     *
     * @param id идентификатор комментария новости
     * @return ResponseEntity с данными комментария новости
     */
    @GetMapping("/{id}")
    CommentDto getCommentById(@PathVariable("id") Long id);

    /**
     * Поиск комментариев новости по тексту комментария.
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment текст комментария для поиска
     * @return ResponseEntity со страницей комментариев новости
     */
    @GetMapping("/search/text/{text}")
    Page<CommentDto> searchCommentsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                          @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                          @PathVariable("text") String fragment);

    /**
     * Поиск комментариев новости по фрагменту имени пользователя
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment фрагмент имени пользователя для поиска комментариев
     * @return ResponseEntity со страницей комментариев новости
     */
    @GetMapping("/search/username/{username}")
    Page<CommentDto> searchCommentByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                             @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                             @PathVariable("username") String fragment);

    /**
     * Создать новый комментарий новости
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             заголовок Authorization
     * @return ResponseEntity с созданным комментарием
     */
    @PostMapping
    CommentDto createComment(@RequestBody CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String auth);

    /**
     * Обновить комментарий новости
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             заголовок Authorization
     * @return ResponseEntity с обновленным комментарием
     */
    @PutMapping
    CommentDto updateComment(@RequestBody CommentUpdateDto commentUpdateDto, @RequestHeader("Authorization") String auth);

    /**
     * Удалить комментарий новости
     *
     * @param id     идентификатор комментария новости
     * @param userId идентификатор пользователя
     * @param auth   заголовок Authorization
     * @return ResponseEntity со статусом NO_CONTENT
     */
    @DeleteMapping("/{id}/{user-id}")
    void deleteComment(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth);
}

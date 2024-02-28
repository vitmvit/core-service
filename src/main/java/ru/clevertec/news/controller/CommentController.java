package ru.clevertec.news.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер комментариев новостей
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * Получить комментарий новости по его идентификатору
     *
     * @param id идентификатор комментария новости
     * @return ResponseEntity с данными комментария новости
     */
    @GetMapping("/{id}")
    ResponseEntity<CommentDto> getNewsCommentById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findCommentById(id));
    }

    /**
     * Поиск комментариев новости по тексту комментария
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment текст комментария для поиска
     * @return ResponseEntity со страницей комментариев новости
     */
    @GetMapping("/search/text/{text}")
    ResponseEntity<Page<CommentDto>> searchCommentsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                          @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                          @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchCommentsByText(offset, limit, fragment));
    }

    /**
     * Поиск комментариев новости по фрагменту имени пользователя
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество комментариев в результате поиска
     * @param fragment фрагмент имени пользователя для поиска комментариев
     * @return ResponseEntity со страницей комментариев новости
     */
    @GetMapping("/search/username/{username}")
    ResponseEntity<Page<CommentDto>> searchCommentsByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                              @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                              @PathVariable("username") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchCommentsByUsername(offset, limit, fragment));
    }

    /**
     * Создать новый комментарий новости
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             заголовок Authorization
     * @return ResponseEntity с созданным комментарием
     */
    @PostMapping
    ResponseEntity<CommentDto> createComment(@RequestBody CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.createComment(commentCreateDto, auth));
    }

    /**
     * Обновить комментарий новости
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             заголовок Authorization
     * @return ResponseEntity с обновленным комментарием
     */
    @PutMapping
    ResponseEntity<CommentDto> updateComment(@RequestBody CommentUpdateDto commentUpdateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(commentUpdateDto, auth));
    }

    /**
     * Удалить комментарий новости
     *
     * @param id     идентификатор комментария новости
     * @param userId идентификатор пользователя
     * @param auth   заголовок Authorization
     * @return ResponseEntity со статусом NO_CONTENT
     */
    @DeleteMapping("/{id}/{user-id}")
    ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth) {
        commentService.deleteComment(id, userId, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

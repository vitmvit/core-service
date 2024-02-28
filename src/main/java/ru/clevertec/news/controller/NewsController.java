package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.service.NewsService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер новостей
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    /**
     * Получить все новости
     *
     * @param offset смещение в результате поиска
     * @param limit  количество новостей в результате поиска
     * @return ResponseEntity с страницей новостей
     */
    @GetMapping
    ResponseEntity<Page<NewsDto>> getAllNews(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                             @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findAllNews(offset, limit));
    }

    /**
     * Получить новость по ее идентификатору
     *
     * @param id идентификатор новости
     * @return ResponseEntity с данными новости
     */
    @GetMapping("/{id}")
    ResponseEntity<NewsDto> getNewsById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findNewsById(id));
    }

    /**
     * Получить новость с комментариями по ее идентификатору
     *
     * @param offset смещение в результате поиска комментариев
     * @param limit  количество комментариев в результате поиска
     * @param id     идентификатор новости
     * @return ResponseEntity с данными новости и комментариями
     */
    @GetMapping("/{id}/comments")
    ResponseEntity<NewsDto> getNewsByIdWithComments(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                    @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                    @PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findNewsByIdWithComments(offset, limit, id));
    }

    /**
     * Поиск новостей по тексту новости
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество новостей в результате поиска
     * @param fragment текст для поиска
     * @return ResponseEntity со страницей найденных новостей
     */
    @GetMapping("/search/text/{text}")
    ResponseEntity<Page<NewsDto>> searchNewsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                   @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchNewsByText(offset, limit, fragment));
    }

    /**
     * Поиск новостей по фрагменту заголовка новости
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество новостей в результате поиска
     * @param fragment фрагмент заголовка для поиска
     * @return ResponseEntity со страницей найденных новостей
     */
    @GetMapping("/search/title/{title}")
    ResponseEntity<Page<NewsDto>> searchNewsByTitle(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                    @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                    @PathVariable("title") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchNewsByTitle(offset, limit, fragment));
    }

    /**
     * Создать новость
     *
     * @param newsCreateDto данные для создания новости
     * @param auth          заголовок Authorization
     * @return ResponseEntity с созданной новостью
     */
    @PostMapping
    ResponseEntity<NewsDto> createNews(@RequestBody NewsCreateDto newsCreateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.createNews(newsCreateDto, auth));
    }

    /**
     * Обновить новость
     *
     * @param newsUpdateDto данные для обновления новости
     * @param auth          заголовок Authorization
     * @return ResponseEntity с обновленной новостью
     */
    @PutMapping
    ResponseEntity<NewsDto> updateNews(@RequestBody NewsUpdateDto newsUpdateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.updateNews(newsUpdateDto, auth));
    }

    /**
     * Удалить новость
     *
     * @param id     идентификатор новости
     * @param userId идентификатор пользователя
     * @param auth   заголовок Authorization
     * @return ResponseEntity со статусом NO_CONTENT
     */
    @DeleteMapping("/{id}/{user-id}")
    ResponseEntity<Void> deleteNews(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth) {
        newsService.deleteNews(id, userId, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
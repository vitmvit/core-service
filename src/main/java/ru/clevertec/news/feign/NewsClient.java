package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Клиент Feign для взаимодействия с сервисом новостей
 */
@FeignClient(contextId = "newsClient", value = "${feign.news-service.value}", url = "${feign.news-service.url-news}")
public interface NewsClient {

    /**
     * Получить все новости
     *
     * @param offset смещение в результате поиска
     * @param limit  количество новостей в результате поиска
     * @return ResponseEntity с страницей новостей
     */
    @GetMapping
    Page<NewsDto> getAllNews(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                             @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit);

    /**
     * Получить новость по ее идентификатору
     *
     * @param id идентификатор новости
     * @return ResponseEntity с данными новости
     */
    @GetMapping("/{id}")
    NewsDto getNewsById(@PathVariable("id") Long id);

    /**
     * Получить новость с комментариями по ее идентификатору
     *
     * @param offset смещение в результате поиска комментариев
     * @param limit  количество комментариев в результате поиска
     * @param id     идентификатор новости
     * @return ResponseEntity с данными новости и комментариями
     */
    @GetMapping("{id}/comments")
    NewsDto getByIdWithComments(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                @PathVariable("id") Long id);

    /**
     * Поиск новостей по тексту новости
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество новостей в результате поиска
     * @param fragment текст для поиска
     * @return ResponseEntity со страницей найденных новостей
     */
    @GetMapping("/search/text/{text}")
    Page<NewsDto> searchNewsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                   @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                   @PathVariable("text") String fragment);

    /**
     * Поиск новостей по фрагменту заголовка новости
     *
     * @param offset   смещение в результате поиска
     * @param limit    количество новостей в результате поиска
     * @param fragment фрагмент заголовка для поиска
     * @return ResponseEntity со страницей найденных новостей
     */
    @GetMapping("/search/title/{title}")
    Page<NewsDto> searchNewsByTitle(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                    @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                    @PathVariable("title") String fragment);

    /**
     * Создать новость
     *
     * @param newsCreateDto данные для создания новости
     * @param auth          заголовок Authorization
     * @return ResponseEntity с созданной новостью
     */
    @PostMapping
    NewsDto createNews(@RequestBody NewsCreateDto newsCreateDto, @RequestHeader("Authorization") String auth);

    /**
     * Обновить новость
     *
     * @param newsUpdateDto данные для обновления новости
     * @param auth          заголовок Authorization
     * @return ResponseEntity с обновленной новостью
     */
    @PutMapping
    NewsDto updateNews(@RequestBody NewsUpdateDto newsUpdateDto, @RequestHeader("Authorization") String auth);

    /**
     * Удалить новость
     *
     * @param id     идентификатор новости
     * @param userId идентификатор пользователя
     * @param auth   заголовок Authorization
     * @return ResponseEntity со статусом NO_CONTENT
     */
    @DeleteMapping("/{id}/{user-id}")
    void deleteNews(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth);
}

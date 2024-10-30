package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@FeignClient(contextId = "newsClient", value = "${feign.news-service.value}", url = "${feign.news-service.url-news}")
public interface NewsClient {

    @GetMapping("/{id}")
    NewsDto getNewsById(@PathVariable("id") Long id);

    @GetMapping("{id}/comments")
    NewsDto getByIdWithComments(@RequestParam(value = "pageNumber", defaultValue = OFFSET_DEFAULT) Integer pageNumber,
                                @RequestParam(value = "pageSize", defaultValue = LIMIT_DEFAULT) Integer pageSize,
                                @PathVariable("id") Long id);

    @GetMapping
    PageContentDto<NewsDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "text", required = false) String text);

    @PostMapping
    NewsDto create(@RequestBody NewsCreateDto newsCreateDto);

    @PutMapping
    NewsDto update(@RequestBody NewsUpdateDto newsUpdateDto);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
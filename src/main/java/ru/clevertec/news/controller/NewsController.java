package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.service.NewsService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    PageContentDto<NewsDto> getAllNews(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "text", required = false) String text) {
        return newsService.getAll(pageNumber, pageSize, title, text);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    NewsDto getNewsById(@PathVariable("id") Long id) {
        return newsService.getNewsById(id);
    }

    @GetMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    NewsDto getByIdWithComments(@RequestParam(value = "pageNumber", defaultValue = OFFSET_DEFAULT) Integer pageNumber,
                                @RequestParam(value = "pageSize", defaultValue = LIMIT_DEFAULT) Integer pageSize,
                                @PathVariable("id") Long id) {
        return newsService.getByIdWithComments(pageNumber, pageSize, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    NewsDto create(@RequestBody NewsCreateDto newsCreateDto) {
        return newsService.create(newsCreateDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    NewsDto update(@RequestBody NewsUpdateDto newsUpdateDto) {
        return newsService.update(newsUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id) {
        newsService.delete(id);
    }
}
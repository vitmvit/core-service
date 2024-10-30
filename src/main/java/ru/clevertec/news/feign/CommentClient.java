package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@FeignClient(contextId = "commentsClient", value = "${feign.comment-service.value}", url = "${feign.comment-service.url-comments}")
public interface CommentClient {

    @GetMapping("/{id}")
    CommentDto getById(@PathVariable("id") Long id);

    @GetMapping
    PageContentDto<CommentDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                      @RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "text", required = false) String text);

    @GetMapping("newsId/{id}")
    PageContentDto<CommentDto> getByNewsId(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                           @PathVariable("id") Long id);

    @PostMapping
    CommentDto create(@RequestBody CommentCreateDto commentCreateDto);

    @PutMapping
    CommentDto update(@RequestBody CommentUpdateDto commentUpdateDto);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
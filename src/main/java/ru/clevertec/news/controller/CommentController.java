package ru.clevertec.news.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getById(@PathVariable("id") Long id) {
        return commentService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageContentDto<CommentDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                             @RequestParam(value = "username", required = false) String username,
                                             @RequestParam(value = "text", required = false) String text) {
        return commentService.getAll(pageNumber, pageSize, username, text);
    }

    @GetMapping("newsId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageContentDto<CommentDto> getByNewsId(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                                  @PathVariable("id") Long id) {
        return commentService.getByNewsId(pageNumber, pageSize, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestBody CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.update(commentUpdateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        commentService.delete(id);
    }
}
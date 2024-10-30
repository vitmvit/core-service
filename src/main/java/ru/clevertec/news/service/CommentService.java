package ru.clevertec.news.service;

import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

public interface CommentService {

    CommentDto getById(Long id);

    PageContentDto<CommentDto> getAll(int pageNumber, int pageSize, String username, String text);

    PageContentDto<CommentDto> getByNewsId(int pageNumber, int pageSize, Long id);

    CommentDto create(CommentCreateDto dto);

    CommentDto update(CommentUpdateDto dto);

    void delete(Long id);
}
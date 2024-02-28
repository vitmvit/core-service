package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

public interface CommentService {
    CommentDto findCommentById(Long id);

    Page<CommentDto> searchCommentsByText(Integer offset, Integer limit, String fragment);

    Page<CommentDto> searchCommentsByUsername(Integer offset, Integer limit, String fragment);

    CommentDto createComment(CommentCreateDto commentCreateDto, String auth);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto, String auth);

    void deleteComment(Long id, Long userId, String auth);
}

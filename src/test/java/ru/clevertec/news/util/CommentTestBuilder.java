package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
public class CommentTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private Long newsId = 1L;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.of(2024, 1, 3, 9, 12, 15, 156);

    @Builder.Default
    private String username = "NameOne";

    @Builder.Default
    private String text = "Text";

    public CommentDto buildCommentDto() {
        return new CommentDto(id, time, text, username, newsId);
    }

    public CommentCreateDto buildCommentCreateDto() {
        return new CommentCreateDto(text, username, newsId);
    }

    public CommentUpdateDto buildCommentUpdateDto() {
        var comment = new CommentUpdateDto(id);
        comment.setText(text);
        comment.setNewsId(newsId);
        comment.setUsername(username);
        return comment;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJTVUJTQ1JJQkVSIiwidXNlcm5hbWUiOiJTVUJTQ1JJQkVSIiwicm9sZSI6IlNVQlNDUklCRVIiLCJleHAiOjE3MDkxNzU1NTV9.uuZclt5mJniONm3Ax_8zAElwOgzk-QqQtoXgMQqPiXo";
    }
}

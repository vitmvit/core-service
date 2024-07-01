package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder(setterPrefix = "with")
public class NewsTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.of(2024, 1, 3, 9, 12, 15, 156);

    @Builder.Default
    private String title = "titleOne";

    @Builder.Default
    private String text = "Text";

    @Builder.Default
    private List<CommentDto> commentDtoList = List.of();

    @Builder.Default
    private Long userId = 1L;

    public NewsDto buildNewsDto() {
        return new NewsDto(id, time, title, text, commentDtoList, userId);
    }

    public NewsCreateDto buildNewsCreateDto() {
        return new NewsCreateDto(title, text, userId);
    }

    public NewsUpdateDto buildNewsUpdateDto() {
        var news = new NewsUpdateDto(id, title, text, userId);
        return news;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getToken() {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJTVUJTQ1JJQkVSIiwidXNlcm5hbWUiOiJTVUJTQ1JJQkVSIiwicm9sZSI6IlNVQlNDUklCRVIiLCJleHAiOjE3MDkxNzU1NTV9.uuZclt5mJniONm3Ax_8zAElwOgzk-QqQtoXgMQqPiXo";
    }
}

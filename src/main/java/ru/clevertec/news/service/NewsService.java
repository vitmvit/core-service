package ru.clevertec.news.service;

import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

public interface NewsService {

    PageContentDto<NewsDto> getAll(int pageNumber, int pageSize, String title, String text);

    NewsDto getNewsById(Long id);

    NewsDto getByIdWithComments(Integer pageNumber, Integer pageSize, Long id);

    NewsDto create(NewsCreateDto dto);

    NewsDto update(NewsUpdateDto dto);

    void delete(Long id);
}
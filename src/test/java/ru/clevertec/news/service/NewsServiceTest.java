package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.NewsClient;
import ru.clevertec.news.service.impl.NewsServiceImpl;
import ru.clevertec.news.util.CommentTestBuilder;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsClient newsClient;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void findNewsByIdShouldReturnExpectedNewsWhenFound() {
        NewsDto expected = NewsTestBuilder.builder().build().buildNewsDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        Long id = expected.getId();

        when(newsClient.getNewsById(id)).thenReturn(newsDto);

        NewsDto actual = newsService.findNewsById(id);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void findNewsByIdShouldReturnExceptionWhenNotFound() {
        Long id = CommentTestBuilder.builder().build().getId();

        when(newsClient.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    void findNewsByIdWithCommentsShouldReturnExpectedNewsWhenFound() {
        NewsDto expected = NewsTestBuilder.builder().build().buildNewsDto();
        Long id = expected.getId();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        List<CommentDto> commentDtoPage = List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        );
        newsDto.setComments(commentDtoPage);

        when(newsClient.getByIdWithComments(OFFSET, LIMIT, id)).thenReturn(newsDto);

        NewsDto actual = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findNewsByIdWithCommentsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        Long id = CommentTestBuilder.builder().build().getId();

        when(newsClient.getByIdWithComments(OFFSET, LIMIT, id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getByIdWithComments(OFFSET, LIMIT, id));
    }

    @Test
    void findAllNewsShouldReturnExpectedPageNews() {
        Page<NewsDto> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        ));

        when(newsClient.getAllNews(OFFSET, LIMIT)).thenReturn(page);

        var actual = newsService.findAllNews(OFFSET, LIMIT);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(newsClient).getAllNews(OFFSET, LIMIT);
        verifyNoMoreInteractions(newsClient);
    }

    @Test
    void findAllNewsShouldReturnEmptyPageWhenEmptyPageNews() {
        when(newsClient.getAllNews(OFFSET, LIMIT)).thenThrow(EmptyListException.class);

        assertThrows(EmptyListException.class, () -> newsClient.getAllNews(OFFSET, LIMIT));
    }

    @Test
    void searchNewsByTextShouldReturnExpectedPageNews() {
        String text = NewsTestBuilder.builder().build().getText();
        Page<NewsDto> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        ));

        when(newsClient.searchNewsByText(OFFSET, LIMIT, text)).thenReturn(page);

        var actual = newsService.searchNewsByText(OFFSET, LIMIT, text);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(newsClient).searchNewsByText(OFFSET, LIMIT, text);
        verifyNoMoreInteractions(newsClient);
    }

    @Test
    void searchNewsByTextShouldReturnEmptyPageWhenEmptyPageComments() {
        String text = NewsTestBuilder.builder().build().getText();

        when(newsClient.searchNewsByText(OFFSET, LIMIT, text)).thenThrow(EmptyListException.class);

        assertThrows(EmptyListException.class, () -> newsClient.searchNewsByText(OFFSET, LIMIT, text));
    }

    @Test
    void searchNewsByTitleShouldReturnExpectedPageNews() {
        String title = NewsTestBuilder.builder().build().getTitle();
        Page<NewsDto> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNewsDto()
        ));

        when(newsClient.searchNewsByTitle(OFFSET, LIMIT, title)).thenReturn(page);

        var actual = newsClient.searchNewsByTitle(OFFSET, LIMIT, title);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
    }

    @Test
    void searchNewsByTitleShouldReturnEmptyPageWhenEmptyPageNews() {
        String text = NewsTestBuilder.builder().build().getText();

        when(newsClient.searchNewsByTitle(OFFSET, LIMIT, text)).thenThrow(EmptyListException.class);

        assertThrows(EmptyListException.class, () -> newsClient.searchNewsByTitle(OFFSET, LIMIT, text));
    }

    @Test
    void createNewsShouldInvokeRepositoryWithoutNewsId() {
        NewsCreateDto newsToSave = NewsTestBuilder.builder().withId(null).build().buildNewsCreateDto();
        NewsDto expected = NewsTestBuilder.builder().build().buildNewsDto();
        NewsDto dto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        when(newsClient.createNews(newsToSave, token)).thenReturn(dto);

        var actual = newsService.createNews(newsToSave, token);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void updateNewsShouldCallsMergeAndSaveWhenNewsFound() {
        Long id = NewsTestBuilder.builder().build().getId();
        NewsUpdateDto dto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        String token = NewsTestBuilder.builder().build().getToken();

        when(newsClient.getNewsById(id)).thenReturn(newsDto);
        newsService.updateNews(dto, token);

        verify(newsClient, times(1)).getNewsById(id);
    }

    @Test
    void updateNewsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        Long id = NewsTestBuilder.builder().build().getId();

        when(newsClient.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    void deleteNews() {
        Long id = NewsTestBuilder.builder().build().getId();
        Long userId = NewsTestBuilder.builder().build().getId();
        String token = NewsTestBuilder.builder().build().getToken();

        newsService.deleteNews(id, userId, token);

        verify(newsClient).deleteNews(id, userId, token);
    }
}

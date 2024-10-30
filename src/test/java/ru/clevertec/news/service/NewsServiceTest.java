package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.facade.AuthenticationFacade;
import ru.clevertec.news.feign.NewsClient;
import ru.clevertec.news.service.impl.NewsServiceImpl;
import ru.clevertec.news.util.AuthTestBuilder;
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

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void getNewsByIdShouldReturnExpectedNewsWhenFound() {
        var expected = NewsTestBuilder.builder().build().buildNewsDto();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var id = expected.getId();

        when(newsClient.getNewsById(id)).thenReturn(newsDto);

        var actual = newsService.getNewsById(id);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void getNewsByIdShouldReturnExceptionWhenNotFound() {
        var id = CommentTestBuilder.builder().build().getId();

        when(newsClient.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    void getByIdWithCommentsShouldReturnExpectedNewsWhenFound() {
        var expected = NewsTestBuilder.builder().build().buildNewsDto();
        var id = expected.getId();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var commentDtoPage = List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        );
        newsDto.setComments(commentDtoPage);

        when(newsClient.getByIdWithComments(OFFSET, LIMIT, id)).thenReturn(newsDto);

        var actual = newsService.getByIdWithComments(OFFSET, LIMIT, id);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getByIdWithCommentsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var id = CommentTestBuilder.builder().build().getId();

        when(newsClient.getByIdWithComments(OFFSET, LIMIT, id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getByIdWithComments(OFFSET, LIMIT, id));
    }

    @Test
    void getAllNewsShouldReturnExpectedPageNews() {
        var pageContentDto = new PageContentDto<>(
                new PageDto(1, 10, 10, 1L),
                List.of(NewsTestBuilder.builder().build().buildNewsDto())
        );
        var title = "title";
        var text = "text";

        when(newsClient.getAll(OFFSET, LIMIT, title, text)).thenReturn(pageContentDto);

        var actual = newsService.getAll(OFFSET, LIMIT, title, text);

        assertEquals(pageContentDto.page().getTotalElements(), actual.page().getTotalElements());
        verify(newsClient).getAll(OFFSET, LIMIT, title, text);
        verifyNoMoreInteractions(newsClient);
    }

    @Test
    void createShouldInvokeRepositoryWithoutNewsId() {
        var newsToSave = NewsTestBuilder.builder().withId(null).build().buildNewsCreateDto();
        var expected = NewsTestBuilder.builder().build().buildNewsDto();
        var dto = NewsTestBuilder.builder().build().buildNewsDto();

        when(authenticationFacade.getCurrentUserId()).thenReturn(newsToSave.getUserId());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(newsClient.create(newsToSave)).thenReturn(dto);

        var actual = newsService.create(newsToSave);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenCommentFound() {
        var newsToUpdate = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        var expected = NewsTestBuilder.builder().build().buildNewsDto();
        var dto = NewsTestBuilder.builder().build().buildNewsDto();

        when(authenticationFacade.getCurrentUserId()).thenReturn(newsToUpdate.getUserId());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(newsClient.update(newsToUpdate)).thenReturn(dto);

        var actual = newsService.update(newsToUpdate);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var id = NewsTestBuilder.builder().build().getId();

        when(newsClient.getNewsById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsClient.getNewsById(id));
    }

    @Test
    void delete() {
        var id = NewsTestBuilder.builder().build().getId();

        when(authenticationFacade.getCurrentUserId()).thenReturn(NewsTestBuilder.builder().build().getId());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(newsClient.getNewsById(id)).thenReturn(NewsTestBuilder.builder().build().buildNewsDto());

        newsService.delete(id);

        verify(newsClient).delete(id);
    }
}
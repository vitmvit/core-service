//package ru.clevertec.news.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import ru.clevertec.news.dto.CommentDto;
//import ru.clevertec.news.dto.NewsDto;
//import ru.clevertec.news.exception.EntityNotFoundException;
//import ru.clevertec.news.feign.NewsClient;
//import ru.clevertec.news.service.impl.NewsServiceImpl;
//import ru.clevertec.news.util.CommentTestBuilder;
//import ru.clevertec.news.util.NewsTestBuilder;
//
//import java.util.List;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static ru.clevertec.news.constant.Constant.LIMIT;
//import static ru.clevertec.news.constant.Constant.OFFSET;
//
//@ExtendWith(MockitoExtension.class)
//public class NewsServiceTest {
//
//    @Mock
//    private NewsClient newsClient;
//
//    @InjectMocks
//    private NewsServiceImpl newsService;
//
//    @Test
//    void findNewsByIdShouldReturnExpectedNewsWhenFound() {
//        NewsDto expected = NewsTestBuilder.builder().build().buildNewsDto();
//        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
//        Long id = expected.getId();
//
//        when(newsClient.getNewsById(id)).thenReturn(newsDto);
//
//        NewsDto actual = newsService.findNewsById(id);
//
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getText(), actual.getText());
//        assertEquals(expected.getTitle(), actual.getTitle());
//    }
//
////    @Test
////    void findNewsByIdShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
////        var exception = assertThrows(EntityNotFoundException.class, () -> newsClient.getNewsById(null));
////        assertEquals(exception.getClass(), EntityNotFoundException.class);
////    }
//
//    @Test
//    void findCommentByIdShouldReturnExpectedCommentWhenFound() {
//        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
//        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();
//        Long id = commentDto.getId();
//
//        when(newsClient.getCommentById(id)).thenReturn(commentDto);
//
//        CommentDto actual = newsService.findCommentById(id);
//
//        assertEquals(expected.getId(), actual.getId());
//    }
//
//    //    @Test
////    void findNewsByIdWithCommentsShouldReturnExpectedNewsWhenFound() {
////        News expected = NewsTestBuilder.builder().build().buildNews();
////        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
////        Long id = expected.getId();
////
////        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(
////                CommentTestBuilder.builder().build().buildCommentDto()
////        ));
////
////        when(newsClient.getByNewsId(OFFSET, LIMIT, id)).thenReturn(commentDtoPage);
////        when(newsRepository.findById(id)).thenReturn(Optional.of(expected));
////        when(newsConverter.convert(expected)).thenReturn(newsDto);
////
////        NewsDto actual = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);
////
////        assertThat(actual)
////                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
////                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
////                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
////                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime());
////    }
////
////    @Test
////    void findNewsByIdWithCommentsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
////        var exception = assertThrows(Exception.class, () -> newsService.findNewsByIdWithComments(OFFSET, LIMIT, null));
////        assertEquals(exception.getClass(), EntityNotFoundException.class);
////    }
////
//    @Test
//    void findAllNewsShouldReturnExpectedPageNews() {
//        Page<NewsDto> page = new PageImpl<>(List.of(
//                NewsTestBuilder.builder().build().buildNewsDto()
//        ));
//
//        when(newsClient.getAllNews(OFFSET, LIMIT)).thenReturn(page);
//
//        var actual = newsService.findAllNews(OFFSET, LIMIT);
//
//        assertEquals(page.getTotalElements(), actual.getTotalElements());
//        verify(newsClient).getAllNews(OFFSET, LIMIT);
//        verifyNoMoreInteractions(newsClient);
//    }
//
//    //    @Test
////    void findAllNewsShouldReturnEmptyPageWhenEmptyPageNews() {
////        when(newsClient.getAllNews(OFFSET, LIMIT)).thenReturn(Page.empty());
////
////        var exception = assertThrows(Exception.class, () -> newsService.findAllNews(OFFSET, LIMIT));
////        assertEquals(exception.getClass(), EmptyListException.class);
////    }
////
//    @Test
//    void searchNewsByTextShouldReturnExpectedPageNews() {
//        String text = NewsTestBuilder.builder().build().getText();
//        Page<NewsDto> page = new PageImpl<>(List.of(
//                NewsTestBuilder.builder().build().buildNewsDto()
//        ));
//
//        when(newsClient.searchNewsByText(OFFSET, LIMIT, text)).thenReturn(page);
//
//        var actual = newsService.searchNewsByText(OFFSET, LIMIT, text);
//
//        assertEquals(page.getTotalElements(), actual.getTotalElements());
//        verify(newsClient).searchNewsByText(OFFSET, LIMIT, text);
//        verifyNoMoreInteractions(newsClient);
//    }
//
////    @Test
////    void searchCommentsByTextShouldReturnEmptyPageWhenEmptyPageComments() {
////        String text = NewsTestBuilder.builder().build().getText();
////
////        when(newsClient.searchNewsByText(OFFSET, LIMIT, text)).thenReturn(Page.empty());
////
////        var exception = assertThrows(Exception.class, () -> newsService.searchNewsByText(OFFSET, LIMIT, text));
////        assertEquals(exception.getClass(), EmptyListException.class);
////    }
//
//    @Test
//    void searchCommentsByTextShouldReturnExpectedPageComments() {
//        String text = NewsTestBuilder.builder().build().getText();
//        Page<CommentDto> page = new PageImpl<>(List.of(
//                CommentTestBuilder.builder().build().buildCommentDto()
//        ));
//
//        when(newsClient.searchCommentsByText(OFFSET, LIMIT, text)).thenReturn(page);
//
//        var actual = newsClient.searchCommentsByText(OFFSET, LIMIT, text);
//
//        assertEquals(page.getTotalElements(), actual.getTotalElements());
//    }
//
////    @Test
////    void searchCommentsByTextShouldReturnEmptyPageWhenEmptyPageComments() {
////        String text = NewsTestBuilder.builder().build().getText();
////
////        when(newsClient.searchCommentsByText(OFFSET, LIMIT, text)).thenReturn(Page.empty());
////
////        var exception = assertThrows(Exception.class, () -> newsService.searchCommentsByText(OFFSET, LIMIT, text));
////        assertEquals(exception.getClass(), EmptyListException.class);
////    }
//
//
//    @Test
//    void searchNewsByTitleShouldReturnExpectedPageNews() {
//        String title = NewsTestBuilder.builder().build().getTitle();
//        Page<NewsDto> page = new PageImpl<>(List.of(
//                NewsTestBuilder.builder().build().buildNewsDto()
//        ));
//
//        when(newsClient.searchNewsByTitle(OFFSET, LIMIT, title)).thenReturn(page);
//
//        var actual = newsClient.searchNewsByTitle(OFFSET, LIMIT, title);
//
//        assertEquals(page.getTotalElements(), actual.getTotalElements());
//    }
//
////    @Test
////    void searchNewsByTitleShouldReturnEmptyPageWhenEmptyPageNews() {
////        String username = NewsTestBuilder.builder().build().getTitle();
////
////        when(newsClient.searchNewsByTitle(OFFSET, LIMIT, username)).thenReturn(Page.empty());
////
////        var exception = assertThrows(Exception.class, () -> newsService.searchNewsByTitle(OFFSET, LIMIT, username));
////        assertEquals(exception.getClass(), EmptyListException.class);
////    }
//
//    @Test
//    void searchCommentsByUsernameReturnExpectedPageComments() {
//        String username = NewsTestBuilder.builder().build().getText();
//        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(
//                CommentTestBuilder.builder().build().buildCommentDto()
//        ));
//
//        when(newsClient.searchCommentByUsername(OFFSET, LIMIT, username)).thenReturn(commentDtoPage);
//
//        var actual = newsClient.searchCommentByUsername(OFFSET, LIMIT, username);
//
//        assertEquals(commentDtoPage.getTotalElements(), actual.getTotalElements());
//    }
//
////    @Test
////    void searchCommentsByUsernameShouldReturnEmptyPageWhenEmptyPageComments() {
////        String username = NewsTestBuilder.builder().build().getTitle();
////
////        when(newsClient.searchCommentByUsername(OFFSET, LIMIT, username)).thenReturn(Page.empty());
////
////        var exception = assertThrows(Exception.class, () -> newsService.searchCommentsByUsername(OFFSET, LIMIT, username));
////        assertEquals(exception.getClass(), EmptyListException.class);
////    }
//
////    @Test
////    void createNewsShouldInvokeRepositoryWithoutNewsId() {
////        NewsCreateDto newsToSave = NewsTestBuilder.builder().withId(null).build().buildNewsCreateDto();
////        NewsDto expected = NewsTestBuilder.builder().build().buildNewsDto();
////        NewsDto dto = NewsTestBuilder.builder().build().buildNewsDto();
////
////        when(newsClient.createNews(newsToSave)).thenReturn(dto);
////
////        var actual= newsService.createNews(newsToSave);
////
////        assertEquals(expected.getId(), actual.getId());
////        assertEquals(expected.getTitle(), actual.getTitle());
////        assertEquals(expected.getText(), actual.getText());
////        assertEquals(expected.getUserId(), actual.getUserId());
////    }
//
////    @Test
////    void createCommentShouldInvokeRepositoryWithoutCommentId() {
////        CommentCreateDto commentToCreate = CommentTestBuilder.builder().build().buildCommentCreateDto();
////        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
////        CommentDto dto = CommentTestBuilder.builder().build().buildCommentDto();
////
////        when(newsClient.createComment(commentToCreate)).thenReturn(dto);
////
////        var actual = newsService.createComment(commentToCreate);
////        assertEquals(expected.getNewsId(), actual.getNewsId());
////        assertEquals(expected.getUsername(), actual.getUsername());
////        assertEquals(expected.getText(), actual.getText());
////    }
//
////    @Test
////    void updateNewsShouldCallsMergeAndSaveWhenNewsFound() {
////        Long id = NewsTestBuilder.builder().build().getId();
////        NewsUpdateDto dto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
////        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
////
////        when(newsClient.getNewsById(id)).thenReturn(newsDto);
////        newsService.updateNews(dto);
////
////        verify(newsClient, times(1)).getNewsById(id);
////    }
//
////    @Test
////    void updateNewsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
////        Long id = NewsTestBuilder.builder().build().getId();
////        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
////        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
////
////        when(newsClient.getNewsById(id)).thenReturn(newsDto);
////
////        assertThrows(EntityNotFoundException.class, () -> newsService.updateNews(newsUpdateDto));
////        verify(newsClient, times(1)).getNewsById(id);
////    }
//
////    @Test
////    void updateCommentShouldCallsMergeAndSaveWhenCommentFound() {
////        CommentUpdateDto commentToUpdate = CommentTestBuilder.builder().build().buildCommentUpdateDto();
////        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
////        CommentDto dto = CommentTestBuilder.builder().build().buildCommentDto();
////
////        when(newsClient.updateComment(commentToUpdate)).thenReturn(dto);
////
////        var actual = newsService.updateComment(commentToUpdate);
////        assertEquals(expected.getNewsId(), actual.getNewsId());
////        assertEquals(expected.getUsername(), actual.getUsername());
////        assertEquals(expected.getText(), actual.getText());
////    }
//
////    @Test
////    void updateCommentShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
////        CommentUpdateDto dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
////
////        when(newsClient.updateComment(dto)).thenThrow(EntityNotFoundException.class);
////
////        assertThrows(EntityNotFoundException.class, () -> newsService.updateComment(dto));
////    }
//
////    @Test
////    void deleteNews() {
////        Long id = NewsTestBuilder.builder().build().getId();
////        newsService.deleteNews(id);
////        verify(newsClient).deleteNews(id);
////    }
//
////    @Test
////    void deleteComment() {
////        Long id = CommentTestBuilder.builder().build().getId();
////        newsService.deleteComment(id);
////        verify(newsClient).deleteComment(id);
////    }
//}

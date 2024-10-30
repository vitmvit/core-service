package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.facade.AuthenticationFacade;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.impl.CommentServiceImpl;
import ru.clevertec.news.util.AuthTestBuilder;
import ru.clevertec.news.util.CommentTestBuilder;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentClient commentClient;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void getByIdShouldReturnExpectedCommentWhenFound() {
        var expected = CommentTestBuilder.builder().build().buildCommentDto();
        var id = expected.getId();

        when(commentClient.getById(id)).thenReturn(CommentTestBuilder.builder().build().buildCommentDto());

        var actual = commentService.getById(id);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getByIdShouldReturnExceptionWhenNotFound() {
        var id = CommentTestBuilder.builder().build().getId();

        when(commentClient.getById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentClient.getById(id));
    }

    @Test
    void createShouldInvokeRepositoryWithoutCommentId() {
        var commentToCreate = CommentTestBuilder.builder().build().buildCommentCreateDto();
        var expected = CommentTestBuilder.builder().build().buildCommentDto();
        var dto = CommentTestBuilder.builder().build().buildCommentDto();

        when(authenticationFacade.getCurrentUsername()).thenReturn(commentToCreate.getUsername());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(commentClient.create(commentToCreate)).thenReturn(dto);

        var actual = commentService.create(commentToCreate);

        assertEquals(expected.getNewsId(), actual.getNewsId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenCommentFound() {
        var commentToUpdate = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        var expected = CommentTestBuilder.builder().build().buildCommentDto();
        var dto = CommentTestBuilder.builder().build().buildCommentDto();

        when(authenticationFacade.getCurrentUsername()).thenReturn(commentToUpdate.getUsername());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(commentClient.update(commentToUpdate)).thenReturn(dto);

        var actual = commentService.update(commentToUpdate);

        assertEquals(expected.getNewsId(), actual.getNewsId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        var dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        when(commentClient.update(dto)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentClient.update(dto));
    }

    @Test
    void delete() {
        var id = CommentTestBuilder.builder().build().getId();

        when(authenticationFacade.getCurrentUsername()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().login());
        when(authenticationFacade.getCurrentUserRole()).thenReturn(AuthTestBuilder.builder().build().buildSignUpDto().role().getRole());
        when(commentClient.getById(id)).thenReturn(CommentTestBuilder.builder().build().buildCommentDto());

        commentService.delete(id);

        verify(commentClient).delete(id);
    }
}
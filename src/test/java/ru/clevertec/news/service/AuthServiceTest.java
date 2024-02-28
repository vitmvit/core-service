//package ru.clevertec.news.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import ru.clevertec.news.dto.auth.JwtDto;
//import ru.clevertec.news.dto.auth.SignUpDto;
//import ru.clevertec.news.dto.constant.RoleName;
//import ru.clevertec.news.model.User;
//import ru.clevertec.news.repository.UserRepository;
//import ru.clevertec.news.service.impl.AuthServiceImpl;
//import ru.clevertec.news.util.AuthTestBuilder;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//
//
////    JwtDto signUp(SignUpDto dto);
//
//    @Test
//    public void testSignUp_NewUser_Success() {
//        SignUpDto dto = AuthTestBuilder.builder().build().buildSignUpDto();
//        String login = dto.login();
//        JwtDto jwtDto = AuthTestBuilder.builder().build().buildJwtDto();
//        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
//
////        when(userRepository.existsByLogin(login)).thenReturn(false); // Пользователя с таким логином не существует
////        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(new User(login, encryptedPassword, RoleName.USER));
//        when(authService.signUp(dto)).thenReturn(jwtDto);
//
//        JwtDto actual = authService.signUp(dto);
//        Assertions.assertEquals(jwtDto.accessToken(),actual.accessToken());
//
//
//        // Проверка вызова методов репозитория
////        verify(userRepository).existsByLogin(login);
////        verify(userRepository).save(ArgumentMatchers.any(User.class));
////
////        // Проверка возвращаемого значения
////        Assertions.assertNotNull(jwtDto);
//////        Assertions.assertEquals(login, jwtDto.());
//    }
////
////    JwtDto signIn(SignInDto dto);
//}

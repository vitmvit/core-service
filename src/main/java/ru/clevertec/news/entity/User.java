package ru.clevertec.news.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.news.dto.constant.RoleName;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.clevertec.news.constant.Constant.*;

/**
 * Модель пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity(name = "users")
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    /**
     * Конструктор с параметрами.
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @param role     роль пользователя
     */
    public User(String login, String password, RoleName role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Map<RoleName, List<String>> roleMap = Map.of(
                RoleName.ADMIN, List.of(ADMIN_ROLE, USER_ROLE),
                RoleName.JOURNALIST, List.of(JOURNALIST_ROLE, USER_ROLE),
                RoleName.SUBSCRIBER, List.of(SUBSCRIBER_ROLE, USER_ROLE)
        );

        return roleMap.getOrDefault(this.role, List.of(USER_ROLE))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return login;
    }

    /**
     * /* {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * /* {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * /* {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
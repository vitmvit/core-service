# core-service

## Swagger

http://localhost:8080/api/doc/swagger-ui/index.html#/

#### Аутентификация и авторизация:

Реализована аутентификация и авторизация с помощью Spring-Boot и JWT.

Для создания нового пользователя мы отправляем POST на /api/auth/signup конечную точку запрос с телом, содержащим логин,
пароль и одну из доступных ролей.

Доступные роли:

```text
ADMIN, USER, JOURNALIST, SUBSCRIBER
```

### AuthController

#### POST запрос на создание нового пользователя:

Request:

```http request
http://localhost:8080/api/signUp
```

```json
{
  "login": "Admin",
  "password": "admin",
  "role": "ADMIN"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIiwidXNlcm5hbWUiOiJVc2VyIiwiZXhwIjoxNzA2NTUzODMyfQ.fKt7m_e-Thx-JtgjbVnR7RF_9ifqyxjVogTYD2SwCfg"
}
```

Если пользователь существует:

```json
{
  "errorMessage": "Username already exists",
  "errorCode": 404
}
```

#### POST запрос на получение токена аутентификации:

Request:

```http request
http://localhost:8080/api/signIn
```

```json
{
  "login": "Admin",
  "password": "admin"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIiwidXNlcm5hbWUiOiJVc2VyIiwiZXhwIjoxNzA2NTUzODMyfQ.fKt7m_e-Thx-JtgjbVnR7RF_9ifqyxjVogTYD2SwCfg"
}
```

Если пользователь не найден:

```json
{
  "errorMessage": "Username not exists",
  "errorCode": 302
}
```
# core-service

## Задача - Система управления новостями

Разработать RESTful web-service, реализующей функционал для работы с системой управления новостями.

Основные сущности:

- news (новость) содержит поля: id, time, title, text и comments (list).
- comment содержит поля: id, time, text, username и news_id.

#### Требования:

1. Использовать Spring Boot 3.x, Java 17, Gradle и PostgreSQL.
2. Разработать API согласно подходам REST (UI не надо):
    - CRUD для работы с новостью
    - CRUD для работы с комментарием
    - просмотр списка новостей (с пагинацией)
    - просмотр новости с комментариями относящимися к ней (с пагинацией)
    - /news
    - /news/{newsId}
    - /news/{newsId}/comments
    - /news/{newsId}/comments/{commentsId}
    - полнотекстовый поиск по различным параметрам (для новостей и комментариев)
      Для потенциально объемных запросов реализовать постраничность
3. Разместить проект в любом из публичных git-репозиториев (Bitbucket, github, gitlab)
4. Код должен быть легко читаемый и понятный, с использованием паттернов проектирования
5. Реализовать на основе Spring @Profile (e.g. test & prod) подключение к базам данных.
6. Подключить liquibase:

- при запуске сервиса накатываются скрипты на рабочую БД (генерируются необходимые таблицы из одного файла и наполняются
  таблицы данными из другого файла, 20 новостей и 10 комментариев, связанных с каждой новостью
- при запуске тестов должен подхватываться скрипт по генерации необходимых таблиц + накатить данные по заполнению
  таблиц (третий файл)

7. Создать реализацию кэша, для хранения сущностей. Реализовать два алгоритма LRU и LFU. Алгоритм и максимальный размер
   коллекции должны читаться из файла application.yml. Алгоритм работы с кешем:

- GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем
- POST - сохраняем в dao и потом сохраняем в кеше
- DELETE - удаляем из dao и потом удаляем в кеша
- PUT - обновление/вставка в dao и потом обновление/вставка в кеше.

8. Весь код должен быть покрыт юнит-тестами (80%) (сервисный слой – 100%)
9. Реализовать логирование запрос-ответ в аспектном стиле (для слоя Controlles), а также логирование по уровням в
   отдельных слоях приложения, используя logback
10. Предусмотреть обработку исключений и интерпретацию их согласно REST (
    см. https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
11. Все настройки должны быть вынесены в *.yml
12. Код должен быть документирован @JavaDoc, а назначение приложения и его интерфейс и настройки должны быть описаны в
    README.md файле
13. Использовать Spring REST Docs или другие средства автоматического документирования (например
    asciidoctor https://asciidoctor.org/docs/asciidoctor-gradle-plugin/ и т.д) и/или Swagger (OpenAPI 3.0)
14. Использовать testcontainers в тестах на persistence layer (для БД)
15. Написать интеграционные тесты
16. Использовать WireMock в тестах для слоя clients (разбиение на микросервисы)
17. Использовать Docker (написать Dockerfile – для spring boot приложения, docker-compose.yml для поднятия БД и
    приложения в контейнерах и настроить взаимодействие между ними)
18. _*_ Подключить кэш провайдер Redis (в docker) (в случае реализации, использовать @Profile для переключения между
    LRU/LFU и Redis)
19. _*_ Spring Security:

- API для регистрации пользователей с ролями admin/journalist/subscriber
- Администратор (role admin) может производить CRUD-операции со всеми сущностями
- Журналист (role journalist) может добавлять и изменять/удалять только свои новости
- Подписчик (role subscriber) может добавлять и изменять/удалять только свои комментарии
- Незарегистрированные пользователи могут только просматривать новости и комментарии
  Создать отдельный микросервис с реляционной базой (postgreSQL) хранящей информацию о пользователях/ролях. Из главного
  микросервиса (отвечающего за новости) запрашивать эту информацию по REST с использованием spring-cloud-feign-client.

20. _*_ Настроить Spring Cloud Config (вынести в отдельный сервис и настроить разрабатываемый сервис на получение их в
    зависимости от профиля)
21. _*_ Реализацию логирования п.9 и обработку исключений п.10 вынести в отдельные
    spring-boot-starter-ы.
22. ** Сущности веб интерфейса (DTO) должны генерироваться при сборке проекта из .proto файлов (
    см. https://github.com/google/protobuf-gradle-plugin) РЕАЛИЗОВАННО ТОЛЬКО НА МИКРОСЕРВИСЕ AUTH-SERVICE

## Swagger

http://localhost:8080/api/doc/swagger-ui/index.html#/

#### Аутентификация и авторизация:

Реализована аутентификация и авторизация с помощью Spring-Boot и JWT.

Для создания нового пользователя необходимо отправить POST-запрос на конечную точку с телом, содержащим логин, пароль и
одну из доступных ролей.

Доступные роли:

```text
ADMIN, USER, JOURNALIST, SUBSCRIBER
```

## Состав

#### Docker:

- [docker](https://github.com/vitmvit/docker)

#### Микросервисы:

- [core-service](https://github.com/vitmvit/core-service/tree/dev) - единая точка входа в приложение
- [auth-service](https://github.com/vitmvit/auth-service/tree/dev) - микросервис для создания и проверки JWT токена, а
  также проверки доступа пользователя к функционалу
- [news-service](https://github.com/vitmvit/news-service/tree/dev) - микросервис для работы с новостями
- [comments-service](https://github.com/vitmvit/comments-service/tree/dev) - микросервис для работы с комментарими
- [spring-cloud-service](https://github.com/vitmvit/spring-cloud-service/tree/dev) - хранит необходимые yml файлы для
  оставшихся микросервисов

#### Библиотеки:

- [dto-lib](https://github.com/vitmvit/dto-lib/tree/dev) - содержит необходимые dto для всех микросервисов
- [cache-lib](https://github.com/vitmvit/cache-lib/tree/dev) - содержит реализацию кастомных кэшей

#### Стартеты:

- [exception-error-handler-spring-boot-starter](https://github.com/vitmvit/exception-error-handler-spring-boot-starter/tree/dev)
  _-_ реализует отлов ошибок
- [logging-spring-boot-starter](https://github.com/vitmvit/logging-spring-boot-starter/tree/dev) - реализует логирование

## Запуск

Для запуска приложения необходимо выкачать следующие проекты и расположить их в одной папке:

- [docker](https://github.com/vitmvit/docker)
- [core-service](https://github.com/vitmvit/core-service/tree/dev)
- [auth-service](https://github.com/vitmvit/auth-service/tree/dev)
- [news-service](https://github.com/vitmvit/news-service/tree/dev)
- [comments-service](https://github.com/vitmvit/comments-service/tree/dev)
- [spring-cloud-service](https://github.com/vitmvit/spring-cloud-service/tree/dev)

##### Локальный запуск

Профили (указываются в application.yml):

- dev (тут используется кастомная реализация кэша)
- prod (тут подключен redis)

Для локального запуска необходимо иметь установленный и запущенный redis на локальном компьютере. И иметь бд postgres
news_management (можно запустить docker-compose для поднятия бд, закомментировав все контейнеры кроме adminer и
postgres).

Так же необходимо собрать следующие проекты в локальном репозитории maven:

- [dto-lib](https://github.com/vitmvit/dto-lib/tree/dev)
- [cache-lib](https://github.com/vitmvit/cache-lib/tree/dev)
- [exception-error-handler-spring-boot-starter](https://github.com/vitmvit/exception-error-handler-spring-boot-starter/tree/dev)
- [logging-spring-boot-starter](https://github.com/vitmvit/logging-spring-boot-starter/tree/dev)

Порядок запуска микросервисов:

- spring-cloud-service
- core-service
- auth-service
- news-service
- comments-service

##### Запуск в docker

Профили (указываются в application.yml):

- docker

Для запуска в контейнерах необходимо выполнить следующие команды с каждым микросервисом, для того, чтобы в папке
каждого проекта build/libs собрались .jar файлы:

```text
clean -> assemble
```

Перед запуском необходимо запустить docker-compose для создания контейнеров (Перейти в папку где лежит docker-compose и
выполнить команду docker compose up).

[Ссылка для входа в adminer](http://localhost/?pgsql=postgres)

| Поля             | Данные     |
|------------------|------------|
| Движок           | PostgreSQL |
| Сервер           | postgres   |
| Имя пользователя | root       |
| Пароль           | root       |

## Права

Доступные роли:

- ADMIN (доступны CRUD со всеми сущностями)
- JOURNALIST (доступны все GET-запросы и запросы на создание, изменения и удаление только своих новостей)
- SUBSCRIBER (доступны все GET-запросы и запросы на создание, изменения и удаление только своих комментариев)
- USER (доступны только все GET-запросы)

## Реализация

Со всеми запросами на контроллеры News- и Comment- необходимо передавать токен, полученные после авторизации.

### AuthController (Feign)

Protobuf задействован только на auth-service

Контроллер поддерживает следующие операции:

- авторизация пользователя
- аутентификация пользователя
- проверка валидности токена

#### POST JwtDto signUp(@RequestBody @Valid SignUpDto dto):

Request:

```http request
http://localhost:8080/api/auth/signUp
```

```json
{
  "login": "ADMINxN",
  "password": "ADMINxN",
  "role": "ADMIN"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBRE1JTnhOIiwidXNlcm5hbWUiOiJBRE1JTnhOIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzA5MTY4MjcyfQ.qRcg-LXVHWyGJfullnHEY3uJqdSYDUi2bhBJ9wv0RaE"
}
```

Если пользователь уже существует:

```json
{
  "errorMessage": "Username is exists",
  "errorCode": 302
}
```

#### POST JwtDto signIn(@RequestBody @Valid SignInDto dto):

Request:

```http request
http://localhost:8080/api/auth/signIn
```

```json
{
  "login": "ADMINxN",
  "password": "ADMINxN"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBRE1JTnhOIiwidXNlcm5hbWUiOiJBRE1JTnhOIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzA5MTY4NjU3fQ.BDSgW0wG-8Hg_n2vgA9c48gUJS54nUmXvMgHnGpRF1o"
}
```

Если пользователь не найден:

```json
{
  "errorMessage": "Username not exists",
  "errorCode": 302
}
```

#### POST boolean check(@RequestHeader(AUTHORIZATION_HEADER) String auth):

Token in header Authorization

Request:

```http request
http://localhost:8081/api/auth/check

```

Response если токен действителен:

```text
true
```

Response если токен не действителен:

```text
false
```

### NewsController (Feign)

Контроллер поддерживает следующие операции:

- получение новости по id (без комментария)
- получение всех комментариев по id новости
- получение всех новостей (фильтр по фрагменту и заголовку)
- создание новости
- редактирование новости
- удаление новости

#### GET запрос PageContentDto<NewsDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "text", required = false) String text)

Request:

```http request
http://localhost:8080/api/news?offset=0&limit=15&title=Escalates in Troubled
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 2
  },
  "content": [
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 1
    }
  ]
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос getById(Long id)

Request:

```http request
http://localhost:8080/api/news/1
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
  "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
  "userId": 1
}
```

Если новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### POST запрос create(NewsCreateDto newsCreateDto, String auth)

Request:

```http request
http://localhost:8080/api/news
```

Body:

```json
{
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "title": "Political Unrest Escalates in Troubled Region",
  "userId": 1
}
```

Response:

```json
{
  "id": 21,
  "time": "2024-02-20T21:48:42.273",
  "title": "Political Unrest Escalates in Troubled Region",
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "userId": 1
}
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT запрос update(NewsUpdateDto newsUpdateDto, String auth)

Request:

```http request
http://localhost:8080/api/news
```

Body:

```json
{
  "id": 1,
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "title": "Political Unrest Escalates in Troubled Region",
  "userId": 1
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Political Unrest Escalates in Troubled Region",
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "userId": 1
}
```

Если обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос delete(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8080/api/news/1
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

### CommentController (Feign)

Контроллер поддерживает следующие операции:

- получение комментария по id
- получение всех комментариев (с фильтром по фрагменту текста и имени пользователя)
- получение комментария по id новости
- создание комментария
- редактирование комментария
- удаление комментария

#### GET CommentDto getById(@PathVariable("id") Long id):

Request:

```http request
http://localhost:8080/api/comments/1
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T19:06:01.405",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если комментарий не найден:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET PageContentDto<CommentDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @RequestParam(value = "username", required = false) String username, @RequestParam(value = "text", required = false) String text):

Request:

```http request
http://localhost:8080/api/comments?text=inspired&username=ssrunner
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    }
  ]
}
```

Если список пуст:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 0,
    "totalElements": 0
  },
  "content": []
}
```

#### GET PageContentDto<CommentDto> getByNewsId(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @PathVariable("id") Long id):

Request:

```http request
http://localhost:8080/api/comments/newsId/7?pageNumber=1&pageSize=2
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 2,
    "totalPages": 2,
    "totalElements": 3
  },
  "content": [
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    }
  ]
}
```

#### POST CommentDto create(@RequestBody CommentCreateDto commentCreateDto):

Request:

```http request
http://localhost:8080/api/comments
```

Body:

```json
{
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Response:

```json
{
  "id": 12,
  "time": "2024-02-20T21:25:13.167",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT CommentDto update(@RequestBody CommentUpdateDto commentUpdateDto):

Request:

```http request
http://localhost:8080/api/comments
```

Body:

```json
{
  "id": 11,
  "time": "2024-10-29T12:01:26.249",
  "text": "This is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Response:

```json
{
  "id": 11,
  "time": "2024-10-29T12:01:26.249",
  "text": "This is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если обновляемый комментарий не найден:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE void delete(@PathVariable("id") Long id):

Request:

```http request
http://localhost:8080/api/comments/11
```

Если у пользователя нет доступа:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```
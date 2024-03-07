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

## Реализация

В каждом запросе необходимо передавать token в заголовке

### AuthController

Контроллер поддерживает следующие операции:

- авторизация пользователя
- аутентификация пользователя

#### POST запрос на создание нового пользователя:

Request:

```http request
http://localhost:8080/api/signUp
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

Если пользователь существует:

```json
{
  "errorMessage": "Username is exists",
  "errorCode": 302
}
```

#### POST запрос на получение токена аутентификации:

Request:

```http request
http://localhost:8080/api/signIn
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

### NewsController (Feign)

Контроллер поддерживает следующие операции:

- получение новости по id (без комментария)
- получение всех комментариев по id новости
- получение всех новостей
- поиск по фрагменту текста
- поиск по фрагменту заголовка новости
- создание новости
- редактирование новости
- удаление новости

#### GET запрос getAll()

Request:

```http request
http://localhost:8080/api/news?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
  "first": true,
  "empty": false
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

#### GET запрос searchByTitle(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8080/api/news/search/title/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8080/api/news/search/text/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST запрос createNews(NewsCreateDto newsCreateDto, String auth)

Request:

```http request
http://localhost:8080/api/news
```

Body:

```json
{
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "title": "Political Unrest Escalates in Troubled Region",
  "userId": 10
}
```

Response:

```json
{
  "id": 32,
  "time": "2024-02-28T20:14:21.659",
  "title": "Political Unrest Escalates in Troubled Region",
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "userId": 10
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT запрос updateNews(NewsUpdateDto newsUpdateDto, String auth)

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

Если текущий пользователь имеет доступ к этой функции, но обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос deleteNews(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8080/api/news/1/4
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

### CommentController (Feign)

Контроллер поддерживает следующие операции:

- получение комментария по id
- поиск по фрагменту текста
- поиск по фрагменту имени пользователя
- создание комментария
- редактирование комментария
- удаление комментария

#### GET запрос getCommentById(Long id)

Request:

```http request
http://localhost:8080/api/comments/7
```

Response:

```json
{
  "id": 7,
  "time": "2024-02-17T19:06:01.405",
  "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
  "username": "techguru99",
  "newsId": 4
}
```

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getByIdWithComments(Long id)

Request:

```http request
http://localhost:8080/api/news/1/comments?offset=0&limit=15
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
  "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
  "comments": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    }
  ],
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

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8080/api/comments/search/text/t?offset=0&limit=15
```

Response:

```json
{
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
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 10,
      "time": "2024-02-17T19:06:01.405",
      "text": "Thank you for sharing this information. Its eye-opening and thought-provoking. Keep up the great work!",
      "username": "smarthacker76",
      "newsId": 10
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "username123",
      "newsId": 1
    }
  ],
  "number": 0,
  "size": 15,
  "totalElements": 9,
  "totalPages": 1,
  "numberOfElements": 9,
  "hasContent": true,
  "first": true,
  "last": true
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByUsername(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8080/api/comments/search/username/u?offset=0&limit=15
```

Response:

```json
{
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
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "username123",
      "newsId": 1
    }
  ],
  "number": 0,
  "size": 15,
  "totalElements": 8,
  "totalPages": 1,
  "numberOfElements": 8,
  "hasContent": true,
  "first": true,
  "last": true
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST запрос createComment(CommentCreateDto commentCreateDto, String auth)

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
  "id": 21,
  "time": "2024-02-20T21:45:23.910",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT запрос updateComment(CommentUpdateDto commentsUpdateDto, String auth)

Request:

```http request
http://localhost:8080/api/comments
```

Body:

```json
{
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1,
  "id": 2
}
```

Response:

```json
{
  "id": 2,
  "time": "2024-02-17T19:06:01.405",
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь имеет доступ к этой функции, но обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос deleteComment(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8080/api/comments/1/3
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

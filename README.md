# Finance Tracker

## ТЗ
REST API приложение для учета личных финансов.

Пользователь может:
- зарегистрироваться в системе
- просматривать и обновлять свой профиль
- создавать кошельки
- добавлять доходы и расходы
- просматривать свои транзакции
- фильтровать транзакции по кошельку

Также в системе есть роль **ADMIN**, которая может просматривать список пользователей и управлять ими через административные endpoints.

---

## Описание проекта
**Finance Tracker** — это backend-приложение на Spring Boot для учета личных финансов.

Проект реализован в виде REST API с авторизацией, валидацией, документацией Swagger и in-memory базой данных H2.
Приложение запускается без внешней базы данных и готово к проверке сразу после клонирования.

---

## Технологии
- Java 23
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security (HTTP Basic Authentication)
- H2 Database
- SpringDoc OpenAPI / Swagger UI
- Maven
- Lombok
- JUnit 5 / Mockito

---

## Запуск проекта

```bash
mvn clean compile
mvn test
mvn spring-boot:run
После запуска приложение доступно по адресу:

text

http://localhost:8080
Swagger UI
Документация API:

text

http://localhost:8080/swagger-ui.html
OpenAPI JSON:

text

http://localhost:8080/v3/api-docs
Тестовые аккаунты
Email	Пароль	Роль
admin@gmail.com	admin	ADMIN
user@gmail.com	user	USER
Аутентификация — HTTP Basic.

H2 Console
text

http://localhost:8080/h2-console
Параметры подключения:

text

JDBC URL: jdbc:h2:mem:finance
User Name: sa
Password:
Основные API endpoints

Auth

Метод	URL	Описание
POST	/api/auth/register	Регистрация нового пользователя

Profile

Метод	URL	Описание
GET	/api/profile	Получить профиль текущего пользователя
PUT	/api/profile	Обновить профиль текущего пользователя

Wallets

Метод	URL	Описание
GET	/api/wallets	Получить все кошельки пользователя
GET	/api/wallets/{id}	Получить кошелек по id
POST	/api/wallets	Создать кошелек
PUT	/api/wallets/{id}	Обновить кошелек
DELETE	/api/wallets/{id}	Удалить кошелек

Transactions

Метод	URL	Описание
GET	/api/transactions	Получить все транзакции пользователя
GET	/api/transactions?walletId={id}	Получить транзакции по кошельку
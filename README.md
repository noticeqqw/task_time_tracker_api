# Task Time Tracker API

REST-сервис для учёта рабочего времени сотрудников.

# Автор

Разработчик: [Вострецов Никита]
Email: [nik.vostre@list.ru]
GitHub: [https://github.com/noticeqqw]
Telegram: [https://t.me/JavaProDeveloper]

## Стек технологий

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-brightgreen.svg)](https://spring.io/projects/spring-security)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0.4-red.svg)](https://mybatis.org/)
[![JWT](https://img.shields.io/badge/JWT-jjwt_0.12.6-black.svg)](https://github.com/jwtk/jjwt)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![SpringDoc OpenAPI](https://img.shields.io/badge/SpringDoc_OpenAPI-2.8.8-85EA2D.svg)](https://springdoc.org/)
[![Bean Validation](https://img.shields.io/badge/Bean_Validation-Jakarta-yellowgreen.svg)](https://beanvalidation.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-latest-green.svg)](https://site.mockito.org/)
[![Testcontainers](https://img.shields.io/badge/Testcontainers-1.21.0-1D4E89.svg)](https://testcontainers.com/)

---

## Реализованный функционал

### Аутентификация (`/api/auth`)

| Метод | Эндпойнт | Тело запроса | Ответ |
|-------|----------|-------------|-------|
| `POST` | `/api/auth/register` | `username`, `email`, `password`, `confirmPassword` | `201 Created` |
| `POST` | `/api/auth/login` | `username`, `password` | `200 OK` + `{ "token": "..." }` |

---

### Задачи (`/api/tasks`)

| Метод | Эндпойнт | Тело запроса | Ответ |
|-------|----------|-------------|-------|
| `POST` | `/api/tasks` | `name` (обяз.), `description` (опц.) | `201 Created` + `TaskResponse` |
| `GET` | `/api/tasks/{id}` | — | `200 OK` + `TaskResponse` |
| `PATCH` | `/api/tasks/{id}/status` | `status` | `200 OK` + `TaskResponse` |


**TaskResponse:**
```json
{
  "id": 1,
  "name": "Разработать API",
  "description": "REST-сервис на Spring Boot",
  "status": "NEW"
}
```

- Если задача с указанным `id` не найдена — возвращается `404 Not Found`.
- Если передан недопустимый статус — `400 Bad Request`.

---

### Записи времени (`/api/time-records`)

| Метод | Эндпойнт | Параметры | Ответ |
|-------|----------|-----------|-------|
| `POST` | `/api/time-records` | тело: `employeeId`, `taskId`, `startTime`, `endTime`, `description` (опц.) | `201 Created` + `TimeRecordResponse` |
| `GET` | `/api/time-records` | query: `employeeId`, `from`, `to` | `200 OK` + массив `TimeRecordResponse` |

Даты передаются в формате **ISO 8601**: `2025-01-15T09:00:00`.

**TimeRecordResponse:**
```json
{
  "id": 1,
  "employeeId": 42,
  "taskId": 1,
  "startTime": "2025-01-15T09:00:00",
  "endTime": "2025-01-15T11:30:00",
  "description": "Реализация контроллеров"
}
```

---

### Обработка ошибок (`@RestControllerAdvice`)

Все ошибки возвращаются в едином формате:

```json
{
  "status": 404,
  "message": "Task not found with id: 5",
  "timestamp": "2025-01-15T10:00:00"
}
```

| HTTP-статус | Причина |
|-------------|---------|
| `400` | Ошибка валидации входных данных (Bean Validation) или некорректный аргумент |
| `401` | Неверный логин/пароль или отсутствует/просрочен JWT |
| `404` | Запрошенный ресурс не найден |
| `500` | Внутренняя ошибка сервера |

---

### Валидация входных DTO (Bean Validation)

Все входящие запросы валидируются через Bean Validation. При ошибке возвращается `400 Bad Request` с перечислением нарушений.

| DTO | Поле | Правило |
|-----|------|---------|
| `RegisterRequest` | `username` | обязательное, 3–50 символов |
| `RegisterRequest` | `email` | обязательное, валидный email |
| `RegisterRequest` | `password` | обязательное, минимум 6 символов |
| `RegisterRequest` | `confirmPassword` | обязательное |
| `LoginRequest` | `username`, `password` | обязательные |
| `CreateTaskRequest` | `name` | обязательное, максимум 255 символов |
| `UpdateTaskStatusRequest` | `status` | обязательное, только `NEW` / `IN_PROGRESS` / `DONE` |
| `CreateTimeRecordRequest` | `employeeId`, `taskId`, `startTime`, `endTime` | обязательные |

Пример ответа при нарушении валидации:
```json
{
  "status": 400,
  "message": "Task name is required, Email must be valid",
  "timestamp": "2025-01-15T10:00:00"
}
```

---

### Интеграционные тесты DAO-слоя (Testcontainers)

Для тестирования взаимодействия с реальной БД используется Testcontainers — при запуске тестов автоматически поднимается контейнер postgres:16-alpine.
Базовый класс BaseIntegrationTest содержит конфигурацию контейнера и через @DynamicPropertySource подставляет реальные параметры подключения вместо основных.

**`TaskMapperIT`** — интеграционный тест `TaskMapper`:
| Тест | Что проверяется |
|------|-----------------|
| `insert_setsGeneratedId` | После вставки задаче присваивается сгенерированный ID |
| `findById_afterInsert_returnsCorrectTask` | Запись, сохранённая в БД, корректно читается обратно |
| `findById_nonExistentId_returnsNull` | При отсутствии задачи возвращается null |
| `updateStatus_changesStatus` | Изменение статуса корректно сохраняется в БД |

**`TimeRecordMapperIT`** — интеграционный тест `TimeRecordMapper`:
| Тест | Что проверяется |
|------|-----------------|
| `insert_setsGeneratedId` | После вставки записи присваивается сгенерированный ID |
| `findByEmployeeIdAndPeriod_returnsOnlyMatchingRecords` | Фильтрация по employeeId и временному диапазону работает корректно — записи вне периода не попадают в результат |

---

## Запуск приложения

```bash
docker compose up --build
```

После запуска откройте Swagger в браузере:

```
http://localhost:8080/swagger-ui.html
```

### Проверка через Swagger UI

Регистрация - Вход и получение токена - Авторизация в Swagger -Создание задачи - Изменение статуса задачи - Создание записи о времени -Получение записей за период

---

## Тестирование через Postman

В корне проекта находится файл `postman.json` — коллекция Postman с готовыми запросами для всех эндпойнтов.

**Порядок работы с коллекцией:**
1. Register → 2. Login (токен сохранится) → 3. Create Task → 4. Get
Task → 5. Update Status → 6. Create Time Record → 7. Get Time Records

---

## Запуск тестов

```bash
./mvnw test
```

Покрыты unit-тестами: сервисы (`TaskService`, `TimeRecordService`, `UserService`), контроллеры и утилита JWT.

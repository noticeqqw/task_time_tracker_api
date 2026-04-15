package test.cdek.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.cdek.api.dto.CreateTaskRequest;
import test.cdek.api.dto.TaskResponse;
import test.cdek.api.dto.UpdateTaskStatusRequest;
import test.cdek.api.service.TaskService;

// REST контроллер для работы с задачами
// Требуется JWT токен в заголовке Authorization
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Создаёт новую задачу
    // Метод: POST /api/tasks
    // Параметры: CreateTaskRequest с name и опционально description
    // Возвращает: TaskResponse с созданной задачей (201 Created)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    // Получает задачу по ID
    // Метод: GET /api/tasks/{id}
    // Параметры: id - ID задачи
    // Возвращает: TaskResponse с информацией о задаче
    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    // Обновляет статус задачи
    // Метод: PATCH /api/tasks/{id}/status
    // Параметры: id - ID задачи, UpdateTaskStatusRequest с новым статусом
    // Возвращает: TaskResponse с обновленной задачей
    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateStatus(id, request);
    }
}

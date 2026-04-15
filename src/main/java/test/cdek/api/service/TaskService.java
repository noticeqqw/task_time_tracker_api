package test.cdek.api.service;

import org.springframework.stereotype.Service;
import test.cdek.api.dto.CreateTaskRequest;
import test.cdek.api.dto.TaskResponse;
import test.cdek.api.dto.UpdateTaskStatusRequest;
import test.cdek.api.entity.Task;
import test.cdek.api.exception.ResourceNotFoundException;
import test.cdek.api.mapper.TaskMapper;

// Сервис для работы с задачами
@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    // Создаёт новую задачу с начальным статусом NEW
    // Параметры: request - данные новой задачи
    // Возвращает: ответ с созданной задачей
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStatus("NEW");
        taskMapper.insert(task);
        return toResponse(task);
    }

    // Получает задачу по ID
    // Параметры: id - идентификатор задачи
    // Возвращает: ответ с информацией о задаче
    // Выбрасывает исключение если задача не найдена
    public TaskResponse getTask(Long id) {
        Task task = taskMapper.findById(id);
        if (task == null) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        return toResponse(task);
    }

    // Обновляет статус задачи
    // Параметры: id - ID задачи, request - новый статус
    // Возвращает: ответ с обновленной задачей
    // Предварительно проверяет что задача существует
    public TaskResponse updateStatus(Long id, UpdateTaskStatusRequest request) {
        getTask(id);
        taskMapper.updateStatus(id, request.getStatus());
        return getTask(id);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getName(), task.getDescription(), task.getStatus());
    }
}

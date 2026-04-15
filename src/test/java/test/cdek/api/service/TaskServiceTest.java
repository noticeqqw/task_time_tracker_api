package test.cdek.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.cdek.api.dto.CreateTaskRequest;
import test.cdek.api.dto.TaskResponse;
import test.cdek.api.dto.UpdateTaskStatusRequest;
import test.cdek.api.entity.Task;
import test.cdek.api.exception.ResourceNotFoundException;
import test.cdek.api.mapper.TaskMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Mock объект маппера (не реальная БД)
    @Mock
    private TaskMapper taskMapper;

    // Тестируемый сервис (внедряются зависимости через конструктор)
    @InjectMocks
    private TaskService taskService;

    // Тест: при создании задачи статус должен быть NEW
    @Test
    void createTask_setsStatusNew() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setName("Fix bug");
        request.setDescription("Some description");

        TaskResponse response = taskService.createTask(request);

        verify(taskMapper).insert(any(Task.class));
        assertEquals("NEW", response.getStatus());
        assertEquals("Fix bug", response.getName());
    }

    // Тест: получение найденной задачи возвращает корректный ответ
    @Test
    void getTask_found_returnsResponse() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Task 1");
        task.setStatus("NEW");
        when(taskMapper.findById(1L)).thenReturn(task);

        TaskResponse response = taskService.getTask(1L);

        assertEquals(1L, response.getId());
        assertEquals("Task 1", response.getName());
        assertEquals("NEW", response.getStatus());
    }

    // Тест: получение ненайденной задачи выбрасывает исключение
    @Test
    void getTask_notFound_throwsResourceNotFoundException() {
        when(taskMapper.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask(99L));
    }

    // Тест: обновление статуса существующей задачи работает корректно
    @Test
    void updateStatus_taskExists_updatesAndReturns() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Task 1");
        task.setStatus("IN_PROGRESS");
        when(taskMapper.findById(1L)).thenReturn(task);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus("IN_PROGRESS");

        TaskResponse response = taskService.updateStatus(1L, request);

        verify(taskMapper).updateStatus(1L, "IN_PROGRESS");
        assertEquals("IN_PROGRESS", response.getStatus());
    }

    // Тест: обновление статуса ненайденной задачи выбрасывает исключение до попытки обновления
    @Test
    void updateStatus_taskNotFound_throwsExceptionBeforeUpdate() {
        when(taskMapper.findById(99L)).thenReturn(null);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus("DONE");

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateStatus(99L, request));
        // Проверяем что обновление не было попытается (защита от обновления несуществующей записи)
        verify(taskMapper, never()).updateStatus(any(), any());
    }
}

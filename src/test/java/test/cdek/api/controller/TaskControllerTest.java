package test.cdek.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.cdek.api.dto.CreateTaskRequest;
import test.cdek.api.dto.TaskResponse;
import test.cdek.api.dto.UpdateTaskStatusRequest;
import test.cdek.api.exception.ResourceNotFoundException;
import test.cdek.api.service.TaskService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    // Mock сервиса задач
    @Mock
    private TaskService taskService;

    // Тестируемый контроллер (зависимости внедряются через конструктор)
    @InjectMocks
    private TaskController taskController;

    // Тест: создание задачи делегирует вызов сервису и возвращает результат
    @Test
    void createTask_callsServiceAndReturnsResponse() {
        TaskResponse expected = new TaskResponse(1L, "Fix bug", "desc", "NEW");
        when(taskService.createTask(any())).thenReturn(expected);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setName("Fix bug");

        TaskResponse result = taskController.createTask(request);

        verify(taskService).createTask(request);
        assertEquals(1L, result.getId());
        assertEquals("NEW", result.getStatus());
    }

    // Тест: получение задачи передаёт ID сервису и возвращает его результат
    @Test
    void getTask_callsServiceWithCorrectId() {
        TaskResponse expected = new TaskResponse(5L, "Task 5", null, "IN_PROGRESS");
        when(taskService.getTask(5L)).thenReturn(expected);

        TaskResponse result = taskController.getTask(5L);

        verify(taskService).getTask(5L);
        assertEquals(5L, result.getId());
        assertEquals("IN_PROGRESS", result.getStatus());
    }

    // Тест: если сервис выбрасывает исключение, оно пробрасывается дальше
    @Test
    void getTask_serviceThrowsNotFound_exceptionPropagates() {
        when(taskService.getTask(99L)).thenThrow(new ResourceNotFoundException("Task not found: 99"));

        assertThrows(ResourceNotFoundException.class, () -> taskController.getTask(99L));
    }

    // Тест: обновление статуса передаёт параметры сервису и возвращает результат
    @Test
    void updateStatus_callsServiceWithCorrectArgs() {
        TaskResponse expected = new TaskResponse(1L, "Task", null, "DONE");
        when(taskService.updateStatus(eq(1L), any())).thenReturn(expected);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus("DONE");

        TaskResponse result = taskController.updateStatus(1L, request);

        verify(taskService).updateStatus(1L, request);
        assertEquals("DONE", result.getStatus());
    }
}

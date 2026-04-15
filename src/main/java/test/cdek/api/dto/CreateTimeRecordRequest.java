package test.cdek.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// DTO для запроса создания записи времени работы
public class CreateTimeRecordRequest {

    // ID сотрудника
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    // ID задачи, над которой работал сотрудник
    @NotNull(message = "Task ID is required")
    private Long taskId;

    // Время начала работы
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    // Время окончания работы
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    // Описание проделанной работы (опционально)
    private String description;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

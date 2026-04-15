package test.cdek.api.entity;

import java.time.LocalDateTime;

// Сущность записи времени работы, представляет строку в таблице time_records
public class TimeRecord {

    // Уникальный идентификатор записи
    private Long id;

    // ID сотрудника, который записывал время
    private Long employeeId;

    // ID задачи, над которой работал сотрудник
    private Long taskId;

    // Время начала работы над задачей
    private LocalDateTime startTime;

    // Время окончания работы над задачей
    private LocalDateTime endTime;

    // Описание проделанной работы (опционально)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

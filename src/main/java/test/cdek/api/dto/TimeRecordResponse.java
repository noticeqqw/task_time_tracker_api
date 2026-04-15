package test.cdek.api.dto;

import java.time.LocalDateTime;

// DTO для ответа с информацией о записи времени работы
public class TimeRecordResponse {

    // ID записи времени
    private Long id;

    // ID сотрудника
    private Long employeeId;

    // ID задачи
    private Long taskId;

    // Время начала работы
    private LocalDateTime startTime;

    // Время окончания работы
    private LocalDateTime endTime;

    // Описание проделанной работы
    private String description;

    public TimeRecordResponse(Long id, Long employeeId, Long taskId,
                              LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.id = id;
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }
}

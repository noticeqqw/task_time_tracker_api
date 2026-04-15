package test.cdek.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// DTO для запроса изменения статуса задачи
public class UpdateTaskStatusRequest {

    // Новый статус задачи (NEW, IN_PROGRESS или DONE)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "NEW|IN_PROGRESS|DONE", message = "Status must be NEW, IN_PROGRESS or DONE")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

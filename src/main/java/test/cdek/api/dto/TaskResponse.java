package test.cdek.api.dto;

// DTO для ответа с информацией о задаче
public class TaskResponse {

    // ID задачи
    private Long id;

    // Название задачи
    private String name;

    // Описание задачи
    private String description;

    // Текущий статус задачи
    private String status;

    public TaskResponse(Long id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}

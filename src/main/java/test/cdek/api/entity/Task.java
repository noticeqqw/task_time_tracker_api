package test.cdek.api.entity;

// Сущность задачи, представляет строку в таблице tasks
public class Task {

    // Уникальный идентификатор задачи
    private Long id;

    // Название задачи
    private String name;

    // Описание задачи (опционально)
    private String description;

    // Статус задачи: NEW, IN_PROGRESS или DONE
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

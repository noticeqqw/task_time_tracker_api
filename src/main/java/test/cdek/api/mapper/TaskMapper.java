package test.cdek.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import test.cdek.api.entity.Task;

// MyBatis маппер для работы с таблицей tasks
@Mapper
public interface TaskMapper {

    // Создаёт новую запись задачи в БД
    // Параметры: task - объект задачи (id будет сгенерирован базой)
    void insert(Task task);

    // Ищет задачу по ID
    // Параметры: id - идентификатор задачи
    // Возвращает: объект Task если найден, null если не найден
    Task findById(Long id);

    // Обновляет статус задачи
    // Параметры: id - ID задачи, status - новый статус
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}

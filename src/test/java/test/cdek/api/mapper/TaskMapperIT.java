package test.cdek.api.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.cdek.api.BaseIntegrationTest;
import test.cdek.api.entity.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperIT extends BaseIntegrationTest {

    @Autowired
    private TaskMapper taskMapper;

    // Тест: вставка задачи генерирует и устанавливает ID
    @Test
    void insert_setsGeneratedId() {
        Task task = new Task();
        task.setName("Test Task");
        task.setDescription("Description");
        task.setStatus("NEW");

        taskMapper.insert(task);

        assertNotNull(task.getId());
        assertTrue(task.getId() > 0);
    }

    // Тест: найденная задача содержит корректные данные из БД
    @Test
    void findById_afterInsert_returnsCorrectTask() {
        Task task = new Task();
        task.setName("Find Me");
        task.setDescription("Some desc");
        task.setStatus("NEW");
        taskMapper.insert(task);

        Task found = taskMapper.findById(task.getId());

        assertNotNull(found);
        assertEquals("Find Me", found.getName());
        assertEquals("Some desc", found.getDescription());
        assertEquals("NEW", found.getStatus());
    }

    // Тест: поиск несуществующей задачи возвращает null
    @Test
    void findById_nonExistentId_returnsNull() {
        Task found = taskMapper.findById(999999L);

        assertNull(found);
    }

    // Тест: обновление статуса корректно изменяет статус в БД
    @Test
    void updateStatus_changesStatus() {
        Task task = new Task();
        task.setName("Status Task");
        task.setStatus("NEW");
        taskMapper.insert(task);

        taskMapper.updateStatus(task.getId(), "IN_PROGRESS");

        Task updated = taskMapper.findById(task.getId());
        assertEquals("IN_PROGRESS", updated.getStatus());
    }
}

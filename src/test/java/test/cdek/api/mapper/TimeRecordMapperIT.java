package test.cdek.api.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.cdek.api.BaseIntegrationTest;
import test.cdek.api.entity.Task;
import test.cdek.api.entity.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeRecordMapperIT extends BaseIntegrationTest {

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Autowired
    private TaskMapper taskMapper;

    // ID задачи, используется во всех тестах
    private Long taskId;

    // Подготовка перед каждым тестом: создание тестовой задачи
    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setName("Task for time records");
        task.setStatus("IN_PROGRESS");
        taskMapper.insert(task);
        taskId = task.getId();
    }

    // Тест: вставка записи времени генерирует и устанавливает ID
    @Test
    void insert_setsGeneratedId() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1L);
        record.setTaskId(taskId);
        record.setStartTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        record.setEndTime(LocalDateTime.of(2024, 1, 1, 17, 0));

        timeRecordMapper.insert(record);

        assertNotNull(record.getId());
        assertTrue(record.getId() > 0);
    }

    // Тест: поиск по периоду возвращает только записи в этом периоде
    @Test
    void findByEmployeeIdAndPeriod_returnsOnlyMatchingRecords() {
        // Создаём записи в нужном периоде (январь)
        TimeRecord r1 = buildRecord(1L, taskId,
                LocalDateTime.of(2024, 1, 5, 9, 0),
                LocalDateTime.of(2024, 1, 5, 17, 0));
        TimeRecord r2 = buildRecord(1L, taskId,
                LocalDateTime.of(2024, 1, 10, 9, 0),
                LocalDateTime.of(2024, 1, 10, 17, 0));

        // Запись за пределами периода (февраль)
        TimeRecord r3 = buildRecord(1L, taskId,
                LocalDateTime.of(2024, 2, 1, 9, 0),
                LocalDateTime.of(2024, 2, 1, 17, 0));

        timeRecordMapper.insert(r1);
        timeRecordMapper.insert(r2);
        timeRecordMapper.insert(r3);

        // Ищем записи за январь
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);

        List<TimeRecord> result = timeRecordMapper.findByEmployeeIdAndPeriod(1L, from, to);

        // Должны вернуться только 2 записи из января
        assertEquals(2, result.size());
    }

    // Тест: поиск по другому сотруднику возвращает пустой результат
    @Test
    void findByEmployeeIdAndPeriod_wrongEmployee_returnsEmpty() {
        TimeRecord record = buildRecord(1L, taskId,
                LocalDateTime.of(2024, 1, 5, 9, 0),
                LocalDateTime.of(2024, 1, 5, 17, 0));
        timeRecordMapper.insert(record);

        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);

        // Ищем записи другого сотрудника (ID 999)
        List<TimeRecord> result = timeRecordMapper.findByEmployeeIdAndPeriod(999L, from, to);

        assertTrue(result.isEmpty());
    }

    private TimeRecord buildRecord(Long employeeId, Long taskId,
                                   LocalDateTime start, LocalDateTime end) {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(employeeId);
        record.setTaskId(taskId);
        record.setStartTime(start);
        record.setEndTime(end);
        return record;
    }
}

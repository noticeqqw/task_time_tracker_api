package test.cdek.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.cdek.api.dto.CreateTimeRecordRequest;
import test.cdek.api.dto.TimeRecordResponse;
import test.cdek.api.entity.Task;
import test.cdek.api.entity.TimeRecord;
import test.cdek.api.exception.ResourceNotFoundException;
import test.cdek.api.mapper.TaskMapper;
import test.cdek.api.mapper.TimeRecordMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    // Mock маппера записей времени
    @Mock
    private TimeRecordMapper timeRecordMapper;

    // Mock маппера задач (нужен для проверки существования задачи)
    @Mock
    private TaskMapper taskMapper;

    // Тестируемый сервис (внедряются зависимости через конструктор)
    @InjectMocks
    private TimeRecordService timeRecordService;

    // Тест: создание записи времени с корректными данными работает правильно
    @Test
    void createTimeRecord_happyPath_insertsAndReturns() {
        Task task = new Task();
        task.setId(1L);
        when(taskMapper.findById(1L)).thenReturn(task);

        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(10L);
        request.setTaskId(1L);
        request.setStartTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        request.setEndTime(LocalDateTime.of(2024, 1, 1, 17, 0));
        request.setDescription("Worked on feature");

        TimeRecordResponse response = timeRecordService.createTimeRecord(request);

        verify(timeRecordMapper).insert(any(TimeRecord.class));
        assertEquals(10L, response.getEmployeeId());
        assertEquals(1L, response.getTaskId());
    }

    // Тест: создание записи с несуществующей задачей выбрасывает исключение
    @Test
    void createTimeRecord_taskNotFound_throwsResourceNotFoundException() {
        when(taskMapper.findById(99L)).thenReturn(null);

        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(10L);
        request.setTaskId(99L);
        request.setStartTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        request.setEndTime(LocalDateTime.of(2024, 1, 1, 17, 0));

        assertThrows(ResourceNotFoundException.class, () -> timeRecordService.createTimeRecord(request));
        // Проверяем что запись не была создана
        verify(timeRecordMapper, never()).insert(any());
    }

    // Тест: создание записи где конец раньше начала выбрасывает исключение
    @Test
    void createTimeRecord_endBeforeStart_throwsIllegalArgumentException() {
        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(10L);
        request.setTaskId(1L);
        request.setStartTime(LocalDateTime.of(2024, 1, 1, 17, 0));
        request.setEndTime(LocalDateTime.of(2024, 1, 1, 9, 0));

        assertThrows(IllegalArgumentException.class, () -> timeRecordService.createTimeRecord(request));
        // Проверяем что не были вызваны методы БД (валидация перед проверкой задачи)
        verify(taskMapper, never()).findById(any());
        verify(timeRecordMapper, never()).insert(any());
    }

    // Тест: получение записей за период возвращает список в правильном порядке
    @Test
    void getTimeRecords_returnsMappedList() {
        TimeRecord r1 = new TimeRecord();
        r1.setId(1L);
        r1.setEmployeeId(10L);
        r1.setTaskId(1L);
        r1.setStartTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        r1.setEndTime(LocalDateTime.of(2024, 1, 1, 12, 0));

        TimeRecord r2 = new TimeRecord();
        r2.setId(2L);
        r2.setEmployeeId(10L);
        r2.setTaskId(2L);
        r2.setStartTime(LocalDateTime.of(2024, 1, 2, 9, 0));
        r2.setEndTime(LocalDateTime.of(2024, 1, 2, 18, 0));

        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);
        when(timeRecordMapper.findByEmployeeIdAndPeriod(10L, from, to)).thenReturn(List.of(r1, r2));

        List<TimeRecordResponse> result = timeRecordService.getTimeRecords(10L, from, to);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    // Тест: получение записей за период когда их нет возвращает пустой список
    @Test
    void getTimeRecords_emptyResult_returnsEmptyList() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);
        when(timeRecordMapper.findByEmployeeIdAndPeriod(10L, from, to)).thenReturn(List.of());

        List<TimeRecordResponse> result = timeRecordService.getTimeRecords(10L, from, to);

        assertTrue(result.isEmpty());
    }
}

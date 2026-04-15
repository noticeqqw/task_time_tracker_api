package test.cdek.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.cdek.api.dto.CreateTimeRecordRequest;
import test.cdek.api.dto.TimeRecordResponse;
import test.cdek.api.service.TimeRecordService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordControllerTest {

    // Mock сервиса записей времени
    @Mock
    private TimeRecordService timeRecordService;

    // Тестируемый контроллер (зависимости внедряются через конструктор)
    @InjectMocks
    private TimeRecordController timeRecordController;

    // Тест: создание записи времени делегирует вызов сервису и возвращает результат
    @Test
    void createTimeRecord_callsServiceAndReturnsResponse() {
        TimeRecordResponse expected = new TimeRecordResponse(
                1L, 10L, 2L,
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2024, 1, 1, 17, 0),
                "Worked on feature"
        );
        when(timeRecordService.createTimeRecord(any())).thenReturn(expected);

        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(10L);
        request.setTaskId(2L);
        request.setStartTime(LocalDateTime.of(2024, 1, 1, 9, 0));
        request.setEndTime(LocalDateTime.of(2024, 1, 1, 17, 0));

        TimeRecordResponse result = timeRecordController.createTimeRecord(request);

        verify(timeRecordService).createTimeRecord(request);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getEmployeeId());
    }

    // Тест: получение записей времени передаёт параметры сервису и возвращает результат
    @Test
    void getTimeRecords_callsServiceAndReturnsList() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);

        TimeRecordResponse r1 = new TimeRecordResponse(1L, 10L, 1L, from, to, null);
        TimeRecordResponse r2 = new TimeRecordResponse(2L, 10L, 2L, from, to, null);
        when(timeRecordService.getTimeRecords(10L, from, to)).thenReturn(List.of(r1, r2));

        List<TimeRecordResponse> result = timeRecordController.getTimeRecords(10L, from, to);

        verify(timeRecordService).getTimeRecords(10L, from, to);
        assertEquals(2, result.size());
    }

    // Тест: получение записей когда их нет возвращает пустой список
    @Test
    void getTimeRecords_emptyResult_returnsEmptyList() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 31, 23, 59);
        when(timeRecordService.getTimeRecords(10L, from, to)).thenReturn(List.of());

        List<TimeRecordResponse> result = timeRecordController.getTimeRecords(10L, from, to);

        assertTrue(result.isEmpty());
    }
}

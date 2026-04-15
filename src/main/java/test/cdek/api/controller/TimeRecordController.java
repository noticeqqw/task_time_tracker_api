package test.cdek.api.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.cdek.api.dto.CreateTimeRecordRequest;
import test.cdek.api.dto.TimeRecordResponse;
import test.cdek.api.service.TimeRecordService;

import java.time.LocalDateTime;
import java.util.List;

// REST контроллер для работы с записями времени работы
// Требуется JWT токен в заголовке Authorization
@RestController
@RequestMapping("/api/time-records")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    public TimeRecordController(TimeRecordService timeRecordService) {
        this.timeRecordService = timeRecordService;
    }

    // Создаёт новую запись времени работы
    // Метод: POST /api/time-records
    // Параметры: CreateTimeRecordRequest с employeeId, taskId, startTime, endTime и опционально description
    // Возвращает: TimeRecordResponse с созданной записью (201 Created)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeRecordResponse createTimeRecord(@Valid @RequestBody CreateTimeRecordRequest request) {
        return timeRecordService.createTimeRecord(request);
    }

    // Получает записи времени работы сотрудника за период
    // Метод: GET /api/time-records
    // Параметры: employeeId - ID сотрудника, from - начало периода, to - конец периода
    // Примечание: даты передаются в формате ISO
    // Возвращает: список TimeRecordResponse за указанный период
    @GetMapping
    public List<TimeRecordResponse> getTimeRecords(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return timeRecordService.getTimeRecords(employeeId, from, to);
    }
}

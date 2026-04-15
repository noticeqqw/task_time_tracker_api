package test.cdek.api.service;

import org.springframework.stereotype.Service;
import test.cdek.api.dto.CreateTimeRecordRequest;
import test.cdek.api.dto.TimeRecordResponse;
import test.cdek.api.entity.TimeRecord;
import test.cdek.api.exception.ResourceNotFoundException;
import test.cdek.api.mapper.TaskMapper;
import test.cdek.api.mapper.TimeRecordMapper;

import java.time.LocalDateTime;
import java.util.List;

// Сервис для работы с записями времени работы
@Service
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;

    public TimeRecordService(TimeRecordMapper timeRecordMapper, TaskMapper taskMapper) {
        this.timeRecordMapper = timeRecordMapper;
        this.taskMapper = taskMapper;
    }

    // Создаёт новую запись времени работы
    // Проверяет что время окончания позже времени начала и что задача существует
    // Параметры: request - данные новой записи
    // Возвращает: ответ с созданной записью
    public TimeRecordResponse createTimeRecord(CreateTimeRecordRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (taskMapper.findById(request.getTaskId()) == null) {
            throw new ResourceNotFoundException("Task not found: " + request.getTaskId());
        }

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setEmployeeId(request.getEmployeeId());
        timeRecord.setTaskId(request.getTaskId());
        timeRecord.setStartTime(request.getStartTime());
        timeRecord.setEndTime(request.getEndTime());
        timeRecord.setDescription(request.getDescription());

        timeRecordMapper.insert(timeRecord);

        return toResponse(timeRecord);
    }

    // Получает все записи времени для сотрудника за указанный период
    // Параметры: employeeId - ID сотрудника, from - начало периода, to - конец периода
    // Возвращает: список записей за этот период
    public List<TimeRecordResponse> getTimeRecords(Long employeeId, LocalDateTime from, LocalDateTime to) {
        return timeRecordMapper.findByEmployeeIdAndPeriod(employeeId, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TimeRecordResponse toResponse(TimeRecord r) {
        return new TimeRecordResponse(r.getId(), r.getEmployeeId(), r.getTaskId(),
                r.getStartTime(), r.getEndTime(), r.getDescription());
    }
}

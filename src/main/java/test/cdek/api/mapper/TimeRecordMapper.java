package test.cdek.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import test.cdek.api.entity.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

// MyBatis маппер для работы с таблицей time_records
@Mapper
public interface TimeRecordMapper {

    // Создаёт новую запись времени работы в БД
    // Параметры: timeRecord - объект записи (id будет сгенерирован базой)
    void insert(TimeRecord timeRecord);

    // Ищет записи времени работы сотрудника за определённый период
    // Параметры: employeeId - ID сотрудника, from - начало периода, to - конец периода
    // Возвращает: список записей TimeRecord за этот период
    List<TimeRecord> findByEmployeeIdAndPeriod(
            @Param("employeeId") Long employeeId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}

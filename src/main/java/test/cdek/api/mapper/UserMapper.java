package test.cdek.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.cdek.api.entity.User;

// MyBatis маппер для работы с таблицей users
@Mapper
public interface UserMapper {

    // Создаёт новую запись пользователя в БД
    // Параметры: user - объект пользователя (id будет сгенерирован базой)
    void insert(User user);

    // Ищет пользователя по имени пользователя
    // Параметры: username - имя для поиска
    // Возвращает: объект User если найден, null если не найден
    User findByUsername(String username);
}

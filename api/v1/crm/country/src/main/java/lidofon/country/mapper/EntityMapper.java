package lidofon.country.mapper;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@Component
public class EntityMapper {
    public <T> Map<String, Object> toMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        System.out.println("fields "+ Arrays.toString(fields));
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field value: " + field.getName(), e);
            }
        }

        return map;
    }
}

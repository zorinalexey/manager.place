package lidofon.country.service.crud;

import lidofon.country.builder.QueryBuilder;
import lidofon.country.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CrudServiceImpl implements CrudService {
    private final EntityMapper entityMapper;

    @Override
    public <T> ResponseEntity<Map<String, Object>> create(T createEntityDto, String table) throws SQLException {
        Map<String, Object> createData = generateCreateData(createEntityDto);
        QueryBuilder.table(table).create(createData).save();
        Object id = createData.get("id");
        Map<String, Object> createdEntity = getCreatedItem((String) id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", createdEntity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Map<String, Object> getCreatedItem(String id) throws SQLException {
        return QueryBuilder
                .table("countries")
                .where("id", id).first();
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> update(T updateEntityDto, String id, String table) throws SQLException {
        Map<String, Object> updatedData = entityMapper.toMap(updateEntityDto);
        try {
            QueryBuilder
                    .table(table)
                    .update(updatedData)
                    .where("id", id)
                    .save();
            Map<String, Object> updatedItem = QueryBuilder.table(table).where("id", id).first();
            Map<String, Object> response = new HashMap<>();
            response.put("data", updatedItem);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> all(T filterDto, String table) {
        return null;
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> paginatedCollection(T filterDto, String table) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> softDelete(String id, String table) throws SQLException {

        try {
            QueryBuilder.table(table).softDelete().where("id",id).save();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "item deleted to trash");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> softDeleteGroup(T deleteFilter, String table) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> hardDelete(String id, String table) {
        return null;
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> hardDeleteGroup(T deleteData, String table) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> restore(String id, String table) {
        return null;
    }

    @Override
    public <T> ResponseEntity<Map<String, Object>> restoreGroup(T restoreData, String table) {
        return null;
    }

    private <T> Map<String, Object> generateCreateData(T createDto) {
        Map<String, Object> createData = entityMapper.toMap(createDto);
        createData.put("id", generateUUID());
        return createData;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

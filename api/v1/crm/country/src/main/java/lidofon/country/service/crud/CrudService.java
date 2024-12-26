package lidofon.country.service.crud;

import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Map;

public interface CrudService {
    <T> ResponseEntity<Map<String,Object>> create(T createEntityDto,String table) throws SQLException;
    <T> ResponseEntity<Map<String,Object>> update(T updateEntityDto,String id,String table) throws SQLException;
    <T> ResponseEntity<Map<String,Object>> all (T filterDto,String table);
    <T> ResponseEntity<Map<String,Object>> paginatedCollection (T filterDto,String table);
    ResponseEntity<Map<String,Object>> softDelete(String id,String table) throws SQLException;
    <T> ResponseEntity<Map<String,Object>> softDeleteGroup(T deleteFilter,String table);

    ResponseEntity<Map<String,Object>> hardDelete(String id,String table);
    <T> ResponseEntity<Map<String,Object>> hardDeleteGroup(T deleteData,String table);
    ResponseEntity<Map<String,Object>> restore(String id,String table);
    <T> ResponseEntity<Map<String,Object>> restoreGroup(T restoreData,String table);

}

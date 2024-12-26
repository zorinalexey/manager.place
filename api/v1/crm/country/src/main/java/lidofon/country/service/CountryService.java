package lidofon.country.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface CountryService {
    public List<Map<String, Object>> getCountriesList() throws SQLException;
    public Map<String,Object> getOne(String id) throws SQLException;
}

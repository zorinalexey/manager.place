package lidofon.country.service.impl;

import lidofon.country.builder.QueryBuilder;
import lidofon.country.mapper.EntityMapper;
import lidofon.country.service.CountryService;
import lidofon.country.service.crud.CrudServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service

public class CountryServiceImpl extends CrudServiceImpl implements CountryService {


    public CountryServiceImpl(EntityMapper entityMapper) {
        super(entityMapper);
    }

    @Override
    public List<Map<String, Object>> getCountriesList() throws SQLException {
        List<Map<String,Object>> result =QueryBuilder.table("countries").get();
        return result;
    }

    @Override
    public Map<String, Object> getOne(String id) throws SQLException {
        Map<String,Object> country = QueryBuilder.table("countries").where("id",id).first();
        return country;
    }
}

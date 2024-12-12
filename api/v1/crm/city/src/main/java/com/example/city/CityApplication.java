package com.example.city;

import com.example.city.builder.QueryBuilder;
import com.example.city.builder.CRUD.enums.JoinType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Slf4j
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CityApplication {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(CityApplication.class, args);
        QueryBuilder builder = new QueryBuilder();
        List<Map<String,String>> citiesByRegion = builder
                .table("cities")
                .select(List.of("name","latitude","longitude"))
                .whereNotIn("region_id",List.of("a1f7645b-ccc1-428a-87a5-9020c3905578"))
                .get();
        System.out.println(citiesByRegion);
    }
}

package com.example.city;

import com.example.city.builder.QueryBuilder;
import com.example.city.builder.CRUD.enums.JoinType;
import com.example.city.builder.StaticQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CityApplication {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(CityApplication.class, args);

        Map<String,Object> firstCity = StaticQueryBuilder
                .table("cities").select(List.of("*"))
                .where("region_id","6db0caaf-56e3-4750-8662-6bf7f11101fb")
                .first();
        System.out.println("First city "+firstCity);
        List<Map<String,Object>> joinResults = StaticQueryBuilder
                .table("cities")
                .select(List.of("regions.name as region_name","cities.name"))
                .join("regions","regions.id","region_id").get();
        System.out.println(joinResults);

    }
}

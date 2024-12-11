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
    private QueryBuilder select;
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(CityApplication.class, args);
        QueryBuilder builder = new QueryBuilder();
//        List<Map<String,String>> list =  builder
//                .table("cities")
//                .select(List.of("name","int_id","region_id")).get();
//        System.out.println(list);
        List<Map<String,String>> list = builder
                .table("cities")
                .select(List.of("cities.id","cities.name","cities.region_id","regions.name AS region_name"))
                .join("regions","cities.region_id","regions.id",JoinType.INNER)
                .get();
        System.out.println(list);
    }
}

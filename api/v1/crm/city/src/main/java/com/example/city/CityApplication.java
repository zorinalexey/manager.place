package com.example.city;

import com.example.city.builder.QueryBuilder;
import com.example.city.builder.CRUD.enums.JoinType;
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
        QueryBuilder builder = new QueryBuilder();

        Map<String, Object> params = Map
                .of(
                        "id", UUID.randomUUID(),
                        "int_id", 1,
                        "name","Стерлитамак",
                        "region_id","a1f7645b-ccc1-428a-87a5-9020c3905578",
                        "country_id",1,
                        "latitude",33,
                        "longitude",45,
                        "zoom",1
                );
        QueryBuilder builderQuery = builder
                .create(params,"cities");
        builderQuery.save();
    }
}

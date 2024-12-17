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


        Map<String, Object> params = Map.of("name","Стерлитамак1");
        QueryBuilder updateOperation = builder.
                select(List.of("name")).table("cities")
                .where("id","da2a9d43-5c80-455c-af21-7bfeda52fb64");
        System.out.println(updateOperation.queryString());
        //String castValue = String.format("CAST(%s as TEXT)", value);
        updateOperation.get();
    }
}

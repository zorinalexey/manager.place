package com.example.city;

import com.example.city.builder.QueryBuilder;
import com.example.city.builder.CRUD.enums.JoinType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CityApplication {
    private QueryBuilder select;
    public static void main(String[] args) {
        SpringApplication.run(CityApplication.class, args);
        QueryBuilder builder = new QueryBuilder();
        QueryBuilder result = builder
                .table("cities")
                .select(List.of("name","region_id"))
                .where("name","Омск");

        result.execute();
    }

}

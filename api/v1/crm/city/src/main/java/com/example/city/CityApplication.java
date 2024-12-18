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
        QueryBuilder builder = new QueryBuilder();


//        Map<String, Object> createParams = Map.of(
//                "name", "Химки",
//                "int_id", 1,
//                "region_id", "6db0caaf-56e3-4750-8662-6bf7f11101fb",
//                "country_id", 1,
//                "latitude", 45.5,
//                "longitude", 37,
//                "zoom", 1
//        );
//
//        QueryBuilder createOp = builder.create(createParams, "cities");
//        createOp.save();

        StaticQueryBuilder items = StaticQueryBuilder
                .update(Map.of("name","ХИМКИ"),"cities")
                .where("id","fd85ca58-adc8-4bbc-bdea-02190ecacc25");
        System.out.println(items);

    }
}

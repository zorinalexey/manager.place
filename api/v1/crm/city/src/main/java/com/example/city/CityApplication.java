package com.example.city;

import com.example.city.builder.CRUD.Select;
import com.example.city.builder.CRUD.enums.JoinType;
import com.example.city.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CityApplication {
    private Select select;
    public static void main(String[] args) {
        SpringApplication.run(CityApplication.class, args);
        Select select = new Select("*","cities");
//        String sql = select
//                .where("order_date", "2023-01-01")
//                .where("order_date","2024-01-01")
//                .or("order_status","shipped")
//                .where("order_amount","100")
//                .where("customer_region","NORTH")
//                .or("customer_region","EAST")
//                .where("payment_method","CARD")
//                .where("active","true")
//                .queryString();
        //System.out.println(sql);
        Select selectAndJoin = new Select("Orders.OrderID, Customers.CustomerName, Orders.OrderDate","Orders");
        selectAndJoin
                .join("Customers","Orders.CustomerID","Customers.CustomerID", JoinType.INNER)
                .where("Orders.CustomerID","1")
                .where("Orders.OrderID","2")
                .or("Orders.OrderID","4");
        String joinQuery = selectAndJoin.queryString();
        System.out.println(joinQuery);

    }

}

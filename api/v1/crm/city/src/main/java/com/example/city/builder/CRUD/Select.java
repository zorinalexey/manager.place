package com.example.city.builder.CRUD;

import com.example.city.builder.CRUD.enums.JoinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Data
@RequiredArgsConstructor
@Slf4j
public class Select {
    private final String columns;
    private final String tableName;
    private List<String> wheres = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private StringBuilder orderBy = new StringBuilder();
    private Map<String, String> binds = new HashMap<>();

    public String queryString() {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT ");
        queryStringBuilder.append(columns);
        queryStringBuilder.append(" FROM ").append(tableName).append(" ");
        //join
        queryStringBuilder.append(String.join(" ",joins)).append(" ");
        log.info("wheres {}", wheres);
        queryStringBuilder.append(String.join(" ", wheres));
        queryStringBuilder.append(orderBy);
        //limit
        return queryStringBuilder.toString();
    }

    public Select where(String column, String value) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
            condition
                    .append(column)
                    .append(" = ")
                    .append(value);
        } else {
            wheres.add("AND");
            condition.append(column).append(" = ").append(value);
        }
        wheres.add(condition.toString());
        return this;
    }

    public Select or(String column, String value) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            throw new RuntimeException("Cant set OR in the beginning of the query");
        } else {
            log.info("last {}",wheres.get(wheres.size()-1));
            if(hasConditionBefore("AND")) {
                wheres.add(wheres.size()-1,"(");
            }
            wheres.add("OR");
            condition.append(column).append(" = ").append(value);
        }
        wheres.add(condition.toString());
        wheres.add(")");
        return this;
    }
    public Select join(String joinTable, String key, String value, JoinType type) {
        StringBuilder condition = new StringBuilder();
        joins.add(type.toString()+" JOIN");
        condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        joins.add(condition.toString());
        return this;
    }
    //SELECT * FROM Customers
    //WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
    private boolean hasConditionBefore(String condition) {
        return wheres.lastIndexOf(condition) < wheres.size() - 1;
    }
}

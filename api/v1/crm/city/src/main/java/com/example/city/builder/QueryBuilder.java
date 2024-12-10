package com.example.city.builder;

import com.example.city.builder.CRUD.enums.JoinType;
import com.example.city.builder.core.Parameter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Data
@RequiredArgsConstructor
@Slf4j
public class QueryBuilder {
    private List<String>  columns = new ArrayList<>();
    private boolean distinct = false;
    private List<String> distinctColumns;

    private String tableName;
    private List<String> wheres = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private StringBuilder orderBy = new StringBuilder();
    private Map<String, String> binds = new HashMap<>();

    private List<Parameter> parameters = new ArrayList<>();

    public String queryString() {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT ");
        if(distinct) {
            queryStringBuilder.append("DISTINCT ").append(String.join(",",distinctColumns));
        } else {
            log.info("columns {}",columns);
            queryStringBuilder.append(String.join(",",columns));
        }
        queryStringBuilder.append(" FROM ").append(tableName).append(" ");
        //join
        queryStringBuilder.append(String.join(" ",joins)).append(" ");
        queryStringBuilder.append(String.join(" ", wheres));
        queryStringBuilder.append(orderBy);
        //limit
        return queryStringBuilder.toString();
    }
    public QueryBuilder table(String tableName) {
        setTableName(tableName);
        return this;
    }
    public QueryBuilder select(List<String> columnList) {
        log.info("columns {}",columnList);
        setColumns(columnList);
        log.info("columns {}",columns);
        return this;
    }

    public QueryBuilder distinct(List<String> columns) {
        setDistinct(true);
        setDistinctColumns(columns);
        return this;
    }

    public QueryBuilder where(String column, String value) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
            condition
                    .append(column)
                    .append(" = ")
                    .append("?");
            parameters.add(new Parameter(value));
        } else {
            wheres.add("AND");
            condition
                    .append(column)
                    .append(" = ")
                    .append("?");
            parameters.add(new Parameter(value));
        }
        wheres.add(condition.toString());
        return this;
    }

    public QueryBuilder or(String column, String value) {
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
    public QueryBuilder join(String joinTable, String key, String value, JoinType type) {
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

    public PreparedStatement buildPreparedStatement(Connection connection) throws SQLException {
        String statement = queryString().trim();
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).bind(preparedStatement, i + 1);
        }
        return preparedStatement;
    }


    public void execute() {
        String url = "jdbc:postgresql://localhost:5432/lidofon";
        String username = "user";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = this.buildPreparedStatement(connection);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("User: " + resultSet.getString("name"));
                    columns.forEach(column -> {
                        try {
                            resultSet.getString(column);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

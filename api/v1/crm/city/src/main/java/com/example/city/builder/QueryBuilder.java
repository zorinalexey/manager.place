package com.example.city.builder;

import com.example.city.builder.CRUD.enums.JoinType;
import com.example.city.builder.CRUD.enums.OperationType;
import com.example.city.builder.core.Parameter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@RequiredArgsConstructor
@Slf4j
public class QueryBuilder {
    private List<String> columns = new ArrayList<>();
    private boolean distinct = false;
    private List<String> distinctColumns;

    private String tableName;
    private List<String> wheres = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private StringBuilder orderBy = new StringBuilder();
    private Map<String, String> binds = new HashMap<>();

    private List<Parameter> parameters = new ArrayList<>();
    private Map<String, Object> valueParams = new HashMap<>();
    private OperationType operationType = OperationType.READ;

    public String queryString() {
        if (operationType == OperationType.READ) {
            StringBuilder queryStringBuilder = new StringBuilder("SELECT ");
            if (distinct) {
                queryStringBuilder.append("DISTINCT ").append(String.join(",", distinctColumns));
            } else {
                log.info("columns {}", columns);
                queryStringBuilder.append(String.join(",", columns));
            }
            queryStringBuilder.append(" FROM ").append(tableName).append(" ");
            //join
            queryStringBuilder.append(String.join(" ", joins)).append(" ");
            queryStringBuilder.append(String.join(" ", wheres));
            queryStringBuilder.append(orderBy);
            //limit
            return queryStringBuilder.toString();
        } else if (operationType == OperationType.CREATE) {
            StringBuilder queryStringBuilder = new StringBuilder("INSERT INTO ")
                    .append(tableName)
                    .append(" ");
            List<String> values = new ArrayList<>();
            List<String> keys = new ArrayList<>(valueParams.keySet().stream().toList());
            valueParams.values().stream().toList().forEach(param -> {
                values.add("?");
                parameters.add(new Parameter(param));
            });
            makeDefaultTimeStamps().forEach((k, v) -> {
                keys.add(k);
                values.add("?");
                parameters.add(new Parameter(v));
            });
            queryStringBuilder
                    .append("(")
                    .append(String.join(",", keys)).append(")")
                    .append(" VALUES")
                    .append(" (")
                    .append(String.join(",", values))
                    .append(")");
            System.out.println("params " + parameters.get(parameters.size() - 1) + " " + parameters.get(parameters.size() - 2));
            return queryStringBuilder.toString();
        } else if (operationType == OperationType.UPDATE) {
            StringBuilder queryStringBuilder = new StringBuilder("UPDATE ")
                    .append(tableName).append(" SET ");
            valueParams.entrySet().forEach(entry -> {
                queryStringBuilder.append(entry.getKey()).append(" = ").append("?").append(",");
                parameters.add(new Parameter(entry.getValue()));
            });
            queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);
            queryStringBuilder.append(" ").append(String.join(" ", wheres));
            return queryStringBuilder.toString();
        }
        return "";
    }

    public QueryBuilder table(String tableName) {
        setTableName(tableName);
        return this;
    }

    public QueryBuilder select(List<String> columnList) {
        setColumns(columnList);
        return this;
    }

    public QueryBuilder distinct(List<String> columns) {
        setDistinct(true);
        setDistinctColumns(columns);
        return this;
    }

    public QueryBuilder where(String column, Object value) throws SQLException {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }

        if (isUUID(column, tableName)) {
            condition.append(column).append(" = ?");
            parameters.add(new Parameter(UUID.fromString(value.toString())));
        } else {
            condition.append(column).append(" = ?");
            parameters.add(new Parameter(value));
        }
        wheres.add(condition.toString());
        return this;
    }

    public QueryBuilder whereIn(String column, List<String> values) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        condition.append(column).append(" in (").append("?").append(")");
        parameters.add(new Parameter(values));
        wheres.add(condition.toString());
        return this;
    }

    public QueryBuilder whereNotIn(String column, List<String> values) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        condition.append(column).append(" not").append(" in (").append("?").append(")");
        parameters.add(new Parameter(values));
        wheres.add(condition.toString());
        return this;
    }

    public QueryBuilder or(String column, String value) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            throw new RuntimeException("Cant set OR in the beginning of the query");
        } else {
            if (hasConditionBefore("AND")) {
                wheres.add(wheres.size() - 1, "(");
            }
            wheres.add("OR");
            condition.append(column).append(" = ").append(value);
        }
        wheres.add(condition.toString());
        wheres.add(")");
        return this;
    }

    public QueryBuilder join(String joinTable, String key, String value, JoinType type) throws SQLException {
        StringBuilder condition = new StringBuilder();
        joins.add(type.toString() + " JOIN");

        //value.contains(".id")
        if (isUUID(value, joinTable)) {
            String castValue = String.format("CAST(%s as TEXT)", value);
            condition.append(joinTable).append(" ON ").append(key).append(" = ").append(castValue);
        } else {
            condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        }
        //condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
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
        System.out.println("statement:" + statement);
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).bind(preparedStatement, i + 1);
        }
        return preparedStatement;
    }


    public List<Map<String, String>> get() throws SQLException {

        Connection connection = getConnection();

        PreparedStatement preparedStatement = this.buildPreparedStatement(connection);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<Map<String, String>> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> result = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metaData.getColumnName(i) + " " + resultSet.getString(i));
                    result.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                list.add(result);
            }
            System.out.println();

            return list;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void save() throws SQLException {
        Connection connection = getConnection();

        PreparedStatement preparedStatement = this.buildPreparedStatement(connection);
        try {
            int resultSet = preparedStatement.executeUpdate();
            System.out.println("res set "+resultSet);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/lidofon";
        String username = "user";
        String password = "password";

        return DriverManager.getConnection(url, username, password);
    }

    public QueryBuilder create(Map<String, Object> data, String tableName) throws SQLException {
        setTableName(tableName);
        operationType = OperationType.CREATE;
        valueParams.putAll(data);
        return this;
    }
    public QueryBuilder update(Map<String, Object> updateData,String table) throws SQLException {
        setTableName(table);
        operationType = OperationType.UPDATE;
        valueParams.putAll(updateData);
        return this;
    }

    private boolean isUUID(String column, String table) throws SQLException {
        try (Connection connection = getConnection()) { // Use try-with-resources to avoid leaks
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, table, null)) {
                System.out.println("cols " + columns);
                if (columns.next()) { // Move to the first row
                    String columnType = columns.getString("TYPE_NAME");
                    System.out.println("Column Type: " + columnType);
                    return "uuid".equalsIgnoreCase(columnType); // Check if the type is UUID
                } else {
                    System.out.println("Column not found: " + column);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            throw e; // Rethrow exception for further handling
        }
        return false; // Return false if no matching column is found
    }

    private Map<String, Object> makeDefaultTimeStamps() {
        Map<String, Object> timeStamps = new HashMap<>();
        java.sql.Timestamp currentTime = java.sql.Timestamp.valueOf(LocalDateTime.now());
        timeStamps.put("created_at", currentTime);
        timeStamps.put("updated_at", currentTime);
        return timeStamps;
    }

}

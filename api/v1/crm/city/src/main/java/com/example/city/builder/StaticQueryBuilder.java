package com.example.city.builder;

import com.example.city.builder.CRUD.enums.JoinType;
import com.example.city.builder.CRUD.enums.OperationType;
import com.example.city.builder.core.Parameter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Data
public class StaticQueryBuilder {
    private static List<String> columns = new ArrayList<>();
    private static boolean distinct = false;
    private static List<String> distinctColumns;

    private static String tableName;
    private static List<String> wheres = new ArrayList<>();
    private static List<String> joins = new ArrayList<>();
    private static StringBuilder orderBy = new StringBuilder();
    private static Map<String, String> binds = new HashMap<>();

    private static List<Parameter> parameters = new LinkedList<>();
    private static Map<String, Object> valueParams = new LinkedHashMap<>();
    private static OperationType operationType = OperationType.READ;

    public static String queryString() {
        System.out.println("op type:" + operationType);
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
            return queryStringBuilder.toString();
        } else if (operationType == OperationType.UPDATE) {
            StringBuilder queryStringBuilder = new StringBuilder("UPDATE ")
                    .append(tableName).append(" SET ");
            for (Map.Entry<String, Object> entry : valueParams.entrySet()) {
                queryStringBuilder.append(entry.getKey()).append(" = ").append("?").append(",");
            }
            queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);
            queryStringBuilder.append(" ").append(String.join(" ", wheres));
            return queryStringBuilder.toString();
        } else if (operationType == OperationType.DELETE) {
            StringBuilder queryStringBuilder = new StringBuilder("DELETE FROM ").append(tableName).append(" ");
            queryStringBuilder.append(" ").append(String.join(" ", wheres));
            return queryStringBuilder.toString();
        }
        return "";
    }

    public static StaticQueryBuilder table(String table) {
        tableName = table;
        return getInstance();
    }

    public StaticQueryBuilder select(List<String> columnList) {
        columns = columnList;
        return getInstance();
    }

    public StaticQueryBuilder distinct(List<String> columns) {
        distinct = true;
        distinctColumns = columns;
        return getInstance();
    }

    public StaticQueryBuilder where(String column, Object value) throws SQLException {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        System.out.println("Is uuid ");
        if (isUUID(column, tableName)) {
            String castValue = String.format("CAST(%s as TEXT)", column);
            condition.append(castValue).append(" = ?");
            parameters.add(new Parameter(UUID.fromString(value.toString())));
        } else {
            condition.append(column).append(" = ?");
            parameters.add(new Parameter(value));
        }
        wheres.add(condition.toString());
        return getInstance();
    }

    public StaticQueryBuilder whereIn(String column, List<String> values) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        condition.append(column).append(" in (").append("?").append(")");
        parameters.add(new Parameter(values));
        wheres.add(condition.toString());
        return getInstance();
    }

    public StaticQueryBuilder whereNotIn(String column, List<String> values) {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        condition.append(column).append(" not").append(" in (").append("?").append(")");
        parameters.add(new Parameter(values));
        wheres.add(condition.toString());
        return getInstance();
    }

    public StaticQueryBuilder or(String column, String value) {
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
        return getInstance();
    }

    public StaticQueryBuilder join(String joinTable, String key, String value, JoinType type) throws SQLException {
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
        return getInstance();
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
        System.out.println("stmt "+preparedStatement.toString());
        try {
            boolean resultSet = preparedStatement.execute();
            System.out.println("res set " + resultSet);
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

    public StaticQueryBuilder create(Map<String, Object> data, String table) throws SQLException {
        tableName = table;
        operationType = OperationType.CREATE;
        valueParams.putAll(data);
        return getInstance();
    }

    public static StaticQueryBuilder update(Map<String, Object> updateData, String table) throws SQLException {
        tableName = table;
        operationType = OperationType.UPDATE;
        valueParams.putAll(new LinkedHashMap<>(updateData));
        for (Map.Entry<String, Object> entry : valueParams.entrySet()) {
            parameters.add(new Parameter(entry.getValue()));
        }
        return getInstance();
    }
    public StaticQueryBuilder delete(String table) throws SQLException {
        tableName = table;
        operationType = OperationType.DELETE;
        return getInstance();
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

    private static Map<String, Object> makeDefaultTimeStamps() {
        Map<String, Object> timeStamps = new HashMap<>();
        java.sql.Timestamp currentTime = java.sql.Timestamp.valueOf(LocalDateTime.now());
        timeStamps.put("created_at", currentTime);
        timeStamps.put("updated_at", currentTime);
        return timeStamps;
    }
    private static StaticQueryBuilder getInstance() {
        return new StaticQueryBuilder();
    }
}
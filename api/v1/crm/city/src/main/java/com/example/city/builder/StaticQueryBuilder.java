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
    private static Integer limit;
    private static List<String> joins = new ArrayList<>();
    private static List<String> groupBy = new ArrayList();
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
                if(!columns.isEmpty()) {
                    queryStringBuilder.append(String.join(",", columns));
                } else {
                    queryStringBuilder.append("*");
                }
            }
            queryStringBuilder.append(" FROM ").append(tableName).append(" ");
            //join
            queryStringBuilder.append(String.join(" ", joins)).append(" ");
            queryStringBuilder.append(String.join(" ", wheres));
            //group by
            queryStringBuilder.append(orderBy);
            if (limit!=null) {
                queryStringBuilder.append(" LIMIT ").append(limit);
            }

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

    //String... columns
    public StaticQueryBuilder select(List<String> columnList) {
        columns = columnList;
        return getInstance();
    }
    public StaticQueryBuilder min(String column) {
        columns.add(String.format("MIN (%s)",column));
        return getInstance();
    }

    public StaticQueryBuilder max(String column) {
        columns.add(String.format("MAX (%s)",column));
        return getInstance();
    }

    public StaticQueryBuilder count(String column) {
        columns.add(String.format("COUNT (%s)",column));
        return getInstance();
    }
    public StaticQueryBuilder sum(String column) {
        columns.add(String.format("SUM(%s)",column));
        return getInstance();
    }

    public StaticQueryBuilder avg(String column) {
        columns.add(String.format("AVG(%s)",column));
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
        if (isUUID(column, tableName)) {
            System.out.println("Is uuid column "+column);
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
    public StaticQueryBuilder between(String column, Object min, Object max) throws SQLException {
        StringBuilder condition = new StringBuilder();
        if (wheres.isEmpty()) {
            wheres.add("WHERE");
        } else {
            wheres.add("AND");
        }
        condition.append(" ").append(column).append(" BETWEEN ? AND ? ");
        parameters.add(new Parameter(min));
        parameters.add(new Parameter(max));
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
        condition.append(column).append(" IN (");
        values.forEach(value -> {
            condition.append("?").append(",");
            parameters.add(new Parameter(value));
        });
        condition.deleteCharAt(condition.length() - 1);
        condition.append(")");
        //parameters.add(new Parameter(values));
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

        condition.append(column).append(" NOT IN (");
        values.forEach(value -> {
            condition.append("?").append(",");
            parameters.add(new Parameter(value));
        });
        condition.deleteCharAt(condition.length() - 1);
        condition.append(")");

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
    public StaticQueryBuilder limit(Integer limitNumber) {
        limit = limitNumber;
        return getInstance();
    }

    public StaticQueryBuilder join(String joinTable, String key, String value) throws SQLException {
        StringBuilder condition = new StringBuilder();
        joins.add(JoinType.INNER + " JOIN");
        if (keyOrValueAreUUID(key,value,joinTable,tableName)) {
            String castKey = String.format("CAST(%s as TEXT)", key);
            String castValue = String.format("CAST(%s as TEXT)", value);
            condition.append(joinTable).append(" ON ").append(castKey).append(" = ").append(castValue);
        } else {
            condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        }
        //condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        joins.add(condition.toString());
        return getInstance();
    }

    public StaticQueryBuilder join(String joinTable, String key, String value, JoinType type) throws SQLException {
        StringBuilder condition = new StringBuilder();
        joins.add(type.toString() + " JOIN");

        //value.contains(".id")
        if (keyOrValueAreUUID(key,value,joinTable,tableName)) {
            String castKey = String.format("CAST(%s as TEXT)", key);
            String castValue = String.format("CAST(%s as TEXT)", value);
            condition.append(joinTable).append(" ON ").append(castKey).append(" = ").append(castValue);
        } else {
            condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        }
        //condition.append(joinTable).append(" ON ").append(key).append(" = ").append(value);
        joins.add(condition.toString());
        return getInstance();
    }

    public StaticQueryBuilder groupBy(String column) {
        StringBuilder sentence = new StringBuilder();
        sentence.append("GROUP BY ").append(column);
        groupBy.add(sentence.toString());
        return getInstance();
    }

    private boolean keyOrValueAreUUID(String key, String value, String joinTable,String tableName) throws SQLException {
        return isUUID(value, tableName) || isUUID( key.split("\\.")[1], joinTable);
    }
    //SELECT * FROM Customers
    //WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
    private boolean hasConditionBefore(String condition) {
        return wheres.lastIndexOf(condition) < wheres.size() - 1;
    }

    public PreparedStatement buildPreparedStatement(Connection connection) throws SQLException {
        String statement = queryString().trim();
        System.out.println("stmt "+statement);
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).bind(preparedStatement, i + 1);
        }
        return preparedStatement;
    }

    public Map<String,Object> first() throws SQLException {
        Connection connection = getConnection();
        limit(1);
        PreparedStatement preparedStatement = this.buildPreparedStatement(connection);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Object> result = new HashMap<>();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.put(metaData.getColumnName(i), resultSet.getString(i));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }finally {
            reset();
        }
    }

    public List<Map<String, Object>> get() throws SQLException {

        Connection connection = getConnection();
        System.out.println("Params "+parameters.toString());
        PreparedStatement preparedStatement = this.buildPreparedStatement(connection);
        System.out.println("PreparedStatement "+preparedStatement.toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<Map<String, Object>> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    result.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                list.add(result);
            }
            System.out.println();

            return list;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            reset();
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
        }finally {
            reset();
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
            try (ResultSet columns = metaData.getColumns(null, null, table, column)) {

                if (columns.next()) { // Move to the first row
                    String columnType = columns.getString("TYPE_NAME");
                    System.out.println("column type "+columnType);
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
    public static void reset() {
        columns = new ArrayList<>();
        distinct = false;
        distinctColumns = null;
        tableName = null;
        wheres = new ArrayList<>();
        limit = null;
        joins = new ArrayList<>();
        orderBy = new StringBuilder();
        binds = new HashMap<>();
        parameters = new ArrayList<>();
        valueParams = new HashMap<>();
        operationType = OperationType.READ;
    }
}

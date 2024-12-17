package com.example.city.builder.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import  java.util.UUID;
import java.util.stream.Collectors;
@Data
@AllArgsConstructor
public class Parameter {
    private Object value;
    public void bind(PreparedStatement preparedStatement,int index) throws SQLException {
        if (value instanceof Integer) {
            preparedStatement.setInt(index, (Integer) value);
        } else if (value instanceof String) {
            preparedStatement.setString(index, (String) value);
        } else if (value instanceof Double) {
            preparedStatement.setDouble(index, (Double) value);
        } else if (value instanceof Long) {
            preparedStatement.setLong(index, (Long) value);
        } else if(value instanceof UUID) {
            System.out.println("value is instance of UUID");
            String resultId = UUID.fromString(value.toString()).toString();
            //preparedStatement.setString(index,resultId);
            preparedStatement.setObject(index, resultId);
        } else if (value instanceof Boolean) {
            preparedStatement.setBoolean(index, (Boolean) value);
        } else if (value instanceof java.sql.Timestamp) {
            preparedStatement.setTimestamp(index, (java.sql.Timestamp) value);
        } else if (value instanceof List<?>) {
            System.out.println("value is instance of List "+value.toString());
            List<String> list = ((List<?>) value).stream().map(Object::toString).toList();
            preparedStatement.setObject(index, String.join(",", list));
        } else if (value == null) {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        } else {
            throw new SQLException("Unsupported parameter type: " + value.getClass());
        }
    }
}

package com.example.city.builder.core;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        } else if (value instanceof Boolean) {
            preparedStatement.setBoolean(index, (Boolean) value);
        } else if (value == null) {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        } else {
            throw new SQLException("Unsupported parameter type: " + value.getClass());
        }

    }
}

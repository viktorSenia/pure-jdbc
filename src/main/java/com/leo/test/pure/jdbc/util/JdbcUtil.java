package com.leo.test.pure.jdbc.util;

import java.sql.*;

/**
 * Created by Senchenko Viktor on 26.09.2016.
 */
public class JdbcUtil {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String DB_CONNECTION = "jdbc:mysql://titan/test?serverTimezone=UTC&characterEncoding=utf-8";

    private static final String DB_USER = "win";

    private static final String DB_PASSWORD = "ZbXUXsFYdVuysNG3";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T execute(String query, ResultMapper<T> resultMapper, Object... parameters) {
        try (Connection connection = getConnection();
             PreparedStatement statement = setParameters(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS), parameters);
             ResultSet resultSet = resultSet(statement)) {
            return resultMapper.map(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultSet resultSet(PreparedStatement statement) throws SQLException {
        if (statement.execute())
            return statement.getResultSet();
        return statement.getGeneratedKeys();
    }

    private PreparedStatement setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i].toString());
        }
        return statement;
    }
}

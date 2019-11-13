package com.yun27jin.toby.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JdbcContext {
    private DataSource dataSource;

    public JdbcContext() {
    }

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void executeSql(final String query) throws SQLException {
        this.workWithStatementStrategy(connection -> connection.prepareStatement(query));
    }

    void executeSql(final String query, final String... params) throws SQLException {
        this.workWithStatementStrategy(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                preparedStatement.setString(i + 1, params[i]);
            return preparedStatement;
        });
    }

    <T> Set<T> executeSql(final String query, final RowMapper<T> rowMapper) throws SQLException {
        return this.workWithStatementStrategy(connection -> connection.prepareStatement(query), rowMapper);
    }

    private <T> Set<T> workWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<T> rowMapper) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.dataSource.getConnection();
            preparedStatement = statementStrategy.makePreparedStatement(connection);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<T> set = new HashSet<>();
            while (resultSet.next())
                set.add(rowMapper.mapTo(resultSet));
            return set;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignored) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }


    private void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.dataSource.getConnection();
            preparedStatement = statementStrategy.makePreparedStatement(connection);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignored) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

}

package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Set;

public class UserDao {
    private JdbcContext jdbcContext;
    private DataSource dataSource;

    public UserDao() {
    }

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public UserDao setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public void add(User user) throws SQLException {
        this.jdbcContext.executeSql("INSERT INTO toby.users(id, name, password) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("DELETE FROM toby.users");
    }

    public Set<User> get() throws SQLException {
        return this.jdbcContext.executeSql("SELECT * FROM toby.users",
                resultSet ->
                        new User(resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("password")));
    }

    public User get(String id) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM toby.users WHERE id = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getString("id"));
            user.setPassword(resultSet.getString("password"));
            user.setName(resultSet.getString("name"));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if (user == null)
            throw new EmptyResultDataAccessException(1);
        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            c = this.dataSource.getConnection();
            preparedStatement = c.prepareStatement("SELECT count(*) FROM toby.users");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignored) {
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignored) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

}
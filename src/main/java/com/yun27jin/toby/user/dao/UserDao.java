package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private DataSource dataSource;

    public UserDao() {
    }

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO toby.users(id, password, name) VALUES (?, ?, ?)");
        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getName());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
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

    public void deleteAll() throws SQLException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM toby.users");
        preparedStatement.executeUpdate();
        preparedStatement.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement("SELECT count(*) FROM toby.users");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        c.close();
        return count;
    }

}
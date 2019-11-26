package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;
import com.yun27jin.toby.user.exception.DuplicatedUserIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbc() {
    }

    public UserDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDaoJdbc setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }

    public void add(User user) {
        this.jdbcTemplate.update("INSERT INTO toby.users(id, name, password) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public void legacyAdd(User user) throws DuplicatedUserIdException {
        DataSource dataSource = jdbcTemplate.getDataSource();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataSource.getConnection().prepareStatement("INSERT INTO toby.users(id, name, password) VALUES (?, ?, ?)");
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 1062 || errorCode ==1 || errorCode == -803)
                throw new DuplicatedUserIdException(e);
            else
                throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {

                }
        }

    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM toby.users WHERE id = ?", new Object[]{id}, this.userRowMapper());
    }

    public List<User> get() {
        return this.jdbcTemplate.query("SELECT * FROM toby.users", this.userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (resultSet, rowNum) ->
                new User(resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"));
    }

    public void delete(String id) {
        this.jdbcTemplate.update("DELETE FROM toby.users", new Object[]{id});
    }

    public void delete() {
        this.jdbcTemplate.update("DELETE FROM toby.users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM toby.users", Integer.class);
    }

}
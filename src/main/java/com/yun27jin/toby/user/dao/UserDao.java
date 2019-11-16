package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao() {
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDao setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }

    public void add(User user) {
        this.jdbcTemplate.update("INSERT INTO toby.users(id, name, password) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
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

    public Integer getCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM toby.users", Integer.class);
    }

}
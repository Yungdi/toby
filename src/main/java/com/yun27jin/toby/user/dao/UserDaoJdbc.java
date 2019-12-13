package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.Level;
import com.yun27jin.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
        Level level = user.getLevel();
        if (user.getLevel() == null)
            level = Level.BASIC;
        this.jdbcTemplate.update(
                "INSERT INTO toby.users(id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                level.intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getEmail());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(
                "SELECT * FROM toby.users WHERE id = ?",
                new Object[]{id},
                this.userMapper());
    }

    public List<User> get() {
        return this.jdbcTemplate.query("SELECT * FROM toby.users ORDER BY id", this.userMapper());
    }

    private RowMapper<User> userMapper() {
        return (resultSet, rowNum) ->
                new User(resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        Level.valueOf(Integer.parseInt(resultSet.getString("level"))),
                        resultSet.getInt("login"),
                        resultSet.getInt("recommend"),
                        resultSet.getString("email"));
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

    @Override
    public void update(User user) {
        this.jdbcTemplate.update("UPDATE toby.users SET name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? WHERE id = ?",
                user.getName(), user.getPassword(), user.getLevel().intValue(),
                user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }

}
package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;

import java.sql.*;

public class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao() {
    }

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement psmt = c.prepareStatement("INSERT INTO toby.users(id, password, name) VALUES (?, ?, ?)");
        psmt.setString(1, user.getId());
        psmt.setString(2, user.getPassword());
        psmt.setString(3, user.getName());
        psmt.executeUpdate();
        psmt.close();
        c.close();
    }

    public User get(String id) throws SQLException, ClassNotFoundException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement psmt = c.prepareStatement("SELECT * FROM toby.users");
        ResultSet rs = psmt.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        rs.close();
        psmt.close();
        c.close();
        return user;
    }

}

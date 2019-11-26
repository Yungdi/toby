package com.yun27jin.toby.user.dao;

import com.yun27jin.toby.user.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> get();
    void delete(String id);
    void delete();
    int getCount();
}

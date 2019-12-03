package com.yun27jin.toby.user.service;

import com.yun27jin.toby.user.dao.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService setUserDao(UserDao userDao) {
        this.userDao = userDao;
        return this;
    }


}

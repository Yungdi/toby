package com.yun27jin.toby.user.service;

import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;

import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService setUserDao(UserDao userDao) {
        this.userDao = userDao;
        return this;
    }

    public void upgradeLevels() {
        List<User> users = this.userDao.get();
        for (User user : users) {
            if (user.canUpgradeLevel(user))
                this.upgradeLevel(user);
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

}
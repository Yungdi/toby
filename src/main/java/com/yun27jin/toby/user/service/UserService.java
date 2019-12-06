package com.yun27jin.toby.user.service;

import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDao userDao;
    private DataSource dataSource;

    public UserService setUserDao(UserDao userDao) {
        this.userDao = userDao;
        return this;
    }

    public UserService setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);

        try {
            List<User> users = this.userDao.get();
            for (User user : users) {
                if (user.canUpgradeLevel(user))
                    this.upgradeLevel(user);
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    public static class TestUserService extends UserService {
        private String id;

        TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    public static class TestUserServiceException extends RuntimeException {
    }

}
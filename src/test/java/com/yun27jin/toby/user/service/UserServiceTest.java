package com.yun27jin.toby.user.service;

import com.yun27jin.toby.user.config.DaoFactory;
import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.Level;
import com.yun27jin.toby.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<User> users;

    @Before
    public void setUp() {
        this.userDao.delete();
        this.users = Arrays.asList(
                new User("a", "장윤진", "1234", Level.BASIC, User.MIN_LOGIN_COUNT_FOR_SILVER - 1, 0, "abc@gmail.com"),
                new User("b", "연우", "1234", Level.BASIC, User.MIN_LOGIN_COUNT_FOR_SILVER, 0, "abc@gmail.com"),
                new User("c", "지수", "1234", Level.SILVER, 60, User.MIN_RECOMMEND_COUNT_FOR_GOLD - 1, "abc@gmail.com"),
                new User("d", "김연아", "1234", Level.SILVER, 60, User.MIN_RECOMMEND_COUNT_FOR_GOLD, "abc@gmail.com"),
                new User("e", "천우희", "1234", Level.GOLD, 100, Integer.MAX_VALUE, "abc@gmail.com")
        );
    }

    @Test
    public void bean() {
        Assert.assertThat(this.userService, CoreMatchers.notNullValue());
    }

    @Test
    public void upgradeLevels() throws SQLException {
        for (User user : users) this.userDao.add(user);
        this.userService.upgradeLevels();

        this.checkLevelUpgraded(users.get(0), false);
        this.checkLevelUpgraded(users.get(1), true);
        this.checkLevelUpgraded(users.get(2), false);
        this.checkLevelUpgraded(users.get(3), true);
        this.checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = this.userDao.get(user.getId());
        if (upgraded)
            assertThat(updatedUser.getLevel(), CoreMatchers.is(user.getLevel().nextLevel()));
        else
            assertThat(updatedUser.getLevel(), CoreMatchers.is(user.getLevel()));
    }

    @Test
    public void add() {
        User userWithLevel = this.users.get(4);
        User userWithoutLevel = this.users.get(0);
        userWithoutLevel.setLevel(null);
        this.userDao.add(userWithLevel);
        this.userDao.add(userWithoutLevel);
        Assert.assertThat(this.userDao.get(userWithLevel.getId()).getLevel(), CoreMatchers.is(Level.GOLD));
        Assert.assertThat(this.userDao.get(userWithoutLevel.getId()).getLevel(), CoreMatchers.is(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() {
        for (User user : users) userDao.add(user);

        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (UserService.TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false);
    }

}
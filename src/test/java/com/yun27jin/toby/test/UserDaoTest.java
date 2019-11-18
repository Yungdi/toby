package com.yun27jin.toby.test;

import com.yun27jin.toby.user.config.DaoFactory;
import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;
import com.yun27jin.toby.user.exception.DuplicatedUserIdException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;


    @Before
    public void setUp() {
        this.userDao = this.context.getBean("userDao", UserDao.class);
        this.user1 = new User("jyj", "장윤진", "1234");
        this.user2 = new User("kyn", "김연아", "1234");
        this.user3 = new User("cwh", "천우희", "1234");
        this.userDao.delete();
    }

    @Test
    public void count() {
        this.userDao.delete();
        Assert.assertThat(this.userDao.getCount(), CoreMatchers.is(0));

        this.userDao.add(this.user1);
        Assert.assertThat(this.userDao.getCount(), CoreMatchers.is(1));

        this.userDao.add(this.user2);
        Assert.assertThat(this.userDao.getCount(), CoreMatchers.is(2));

        this.userDao.add(this.user3);
        Assert.assertThat(this.userDao.getCount(), CoreMatchers.is(3));
    }

    @Test
    public void addAndGet() {
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0));

        this.userDao.add(this.user1);
        this.userDao.add(this.user2);
        this.userDao.add(this.user3);

        List<User> userList = this.userDao.get();
        Assert.assertThat(userList, CoreMatchers.hasItems(this.user1, this.user2, this.user3));

        User user4 = this.userDao.get(user1.getId());
        Assert.assertThat(user4.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(user4.getPassword(), CoreMatchers.is(user1.getPassword()));

        User user5 = this.userDao.get(user2.getId());
        Assert.assertThat(user5.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(user5.getPassword(), CoreMatchers.is(user2.getPassword()));

        User user6 = this.userDao.get(user3.getId());
        Assert.assertThat(user6.getName(), CoreMatchers.is(user3.getName()));
        Assert.assertThat(user6.getPassword(), CoreMatchers.is(user3.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() {
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0));
        this.userDao.get("unknown-id");
    }

    @Test(expected = DuplicatedUserIdException.class)
    public void addUserFailure() {
        this.userDao.add(this.user1);
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(1));
        this.userDao.legacyAdd(this.user1);
    }

}

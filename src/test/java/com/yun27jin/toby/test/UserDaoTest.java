package com.yun27jin.toby.test;

import com.yun27jin.toby.user.dao.DaoFactory;
import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        User user = new User("yun27jin", "장윤진", "1234");
        userDao.add(user);
        User user1 = userDao.get("yun27jin");
        Assert.assertThat(user.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(user.getPassword(), CoreMatchers.is(user1.getPassword()));
    }

}

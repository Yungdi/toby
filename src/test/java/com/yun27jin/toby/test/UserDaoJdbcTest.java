package com.yun27jin.toby.test;

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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoJdbcTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        this.user1 = new User("jyj", "장윤진", "1234", Level.BASIC, 1, 0, "a@gmail.com");
        this.user2 = new User("kyn", "김연아", "1234", Level.SILVER, 55, 10, "a@gmail.com");
        this.user3 = new User("cwh", "천우희", "1234", Level.GOLD, 100, 40, "a@gmail.com");
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

        User getUser1 = this.userDao.get(user1.getId());
        Assert.assertThat(getUser1, CoreMatchers.is(this.user1));
        User getUser2 = this.userDao.get(this.user2.getId());
        Assert.assertThat(getUser2, CoreMatchers.is(this.user2));
        User getUser3 = this.userDao.get(this.user3.getId());
        Assert.assertThat(getUser3, CoreMatchers.is(this.user3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() {
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0));
        this.userDao.get("unknown-id");
    }

    @Test(expected = DuplicateKeyException.class)
    public void addUserFailure() {
        this.userDao.add(this.user1);
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(1));
        this.userDao.add(this.user1);
    }

    public void sqlExceptionTranslate() {
        try {
            this.userDao.add(user1);
            this.userDao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlException = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            DataAccessException translatedException = set.translate(null, null, sqlException);
            Assert.assertThat(translatedException, CoreMatchers.instanceOf(DuplicateKeyException.class));
        }
    }

    @Test
    public void update() {
        this.userDao.delete();
        this.userDao.add(this.user1);
        this.userDao.add(this.user2);

        this.user1.setName("윤진");
        this.user1.setPassword("spring");
        this.user1.setLevel(Level.GOLD);
        this.user1.setLogin(1000);
        this.user1.setRecommend(999);

        this.userDao.update(user1);

        User updatedUser = this.userDao.get(this.user1.getId());
        Assert.assertThat(updatedUser, CoreMatchers.is(this.user1));

        User notUpdatedUser = this.userDao.get(this.user2.getId());
        Assert.assertThat(notUpdatedUser, CoreMatchers.is(user2));
    }

}
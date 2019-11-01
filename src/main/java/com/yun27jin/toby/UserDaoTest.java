package com.yun27jin.toby;

import com.yun27jin.toby.user.dao.DaoFactory;
import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean( "userDao", UserDao.class);
        User user = new User();
        user.setId("yun27jin");
        user.setPassword("1234");
        user.setName("장윤진");
        userDao.add(user);
        User user1 = userDao.get("yun22jin");
        System.out.println(user1);
    }

}

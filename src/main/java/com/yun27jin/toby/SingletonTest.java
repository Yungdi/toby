package com.yun27jin.toby;

import com.yun27jin.toby.user.dao.DaoFactory;
import com.yun27jin.toby.user.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {
    public static void main(String[] args) {
        DaoFactory daoFactory = new DaoFactory();
        UserDao userDao1 = daoFactory.userDao();
        UserDao userDao2 = daoFactory.userDao();
        System.out.println(userDao1);
        System.out.println(userDao2);
        System.out.println(userDao1 == userDao2);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao3 = applicationContext.getBean("userDao", UserDao.class);
        UserDao userDao4 = applicationContext.getBean("userDao", UserDao.class);
        System.out.println(userDao3);
        System.out.println(userDao4);
        System.out.println(userDao3 == userDao4);
    }
}

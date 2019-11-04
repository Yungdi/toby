package com.yun27jin.toby;

import com.yun27jin.toby.user.dao.CountingConnectionMaker;
import com.yun27jin.toby.user.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class XmlUserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao xmlUserDao = context.getBean("xmlUserDao", UserDao.class);
        xmlUserDao.get("yun27jin");
        xmlUserDao.get("yun27jin");
        CountingConnectionMaker xmlCountingConnectionMaker = context.getBean("xmlCountingConnectionMaker", CountingConnectionMaker.class);
        System.out.println(xmlCountingConnectionMaker.getCounter());
    }
}

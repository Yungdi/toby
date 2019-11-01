package com.yun27jin.toby;

import com.yun27jin.toby.user.dao.UserDao;
import com.yun27jin.toby.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setId("yun27jin");
        user.setPassword("1234");
        user.setName("장윤진");
        userDao.add(user);
        User user1 = userDao.get("yun22jin");
        System.out.println(user1);
    }

}

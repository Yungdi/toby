package com.yun27jin.toby.user.dao;

public class SingletonUserDao {
    private static SingletonUserDao INSTANCE;

    private SingletonUserDao() {
    }

    public static synchronized SingletonUserDao singletonUserDao() {
        if (INSTANCE == null)
            INSTANCE = new SingletonUserDao();
        return INSTANCE;
    }

}
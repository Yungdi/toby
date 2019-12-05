package com.yun27jin.toby.user.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_COUNT_FOR_GOLD =30;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null)
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다");
        else
            this.level = nextLevel;
    }

    public boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level) {
            case BASIC:
                return user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + level);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login == user.login &&
                recommend == user.recommend &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                level == user.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, level, login, recommend);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .add("level=" + level)
                .add("login=" + login)
                .add("recommend=" + recommend)
                .toString();
    }

}
package com.yun27jin.toby.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {

    T mapTo(ResultSet resultSet) throws SQLException;

}

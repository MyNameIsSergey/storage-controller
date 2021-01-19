package com.flex.storage.dao.mappers;

import com.flex.storage.models.UserModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<UserModel> {
    @Override
    public UserModel mapRow(ResultSet resultSet, int i) throws SQLException {
        UserModel model = new UserModel();
        model.setUsername(resultSet.getString("user_name"));
        model.setEncryptedPassword(resultSet.getString("encrypted_password"));
        model.setEnabled(resultSet.getBoolean("enabled"));
        model.setId(resultSet.getInt("id"));
        return model;
    }
}

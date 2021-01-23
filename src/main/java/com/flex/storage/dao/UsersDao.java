package com.flex.storage.dao;

import com.flex.storage.dao.mappers.UserMapper;
import com.flex.storage.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Transactional
public class UsersDao extends JdbcDaoSupport {

    @Autowired
    public UsersDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void addUser(UserModel user) {
        String sql = "INSERT INTO users (user_name, encrypted_password, enabled) VALUES (?,?,?);" +
                "SELECT LAST_INSERT_ID();";
        Object[] params = {
                user.getUsername(), user.getEncryptedPassword(), user.isEnabled()
        };
        Integer id = this.getJdbcTemplate().queryForObject(sql, Integer.class, params);
        user.setId(id);
    }

    public UserModel findUserAccount(String userName) {
        String sql = "SELECT * FROM users WHERE user_name = ? ";
        Object[] params = new Object[] { userName };
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserModel findUserAccountById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        try {
            return this.getJdbcTemplate().queryForObject(sql, new UserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

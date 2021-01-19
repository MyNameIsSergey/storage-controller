package com.flex.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class RolesDao extends JdbcDaoSupport {

    @Autowired
    public RolesDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void addDefaultRoleToUser(int userId) {
        String sql = "INSERT INTO user_role (user_id) VALUES (?);";
        this.getJdbcTemplate().update(sql, userId);
    }

    public void addRoleToUser(int userId, int roleId) {
        String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?);";
        this.getJdbcTemplate().update(sql, userId, roleId);
    }

    public List<String> getUserRoleNames(int userId) {
        String sql = "SELECT r.name " //
                + " FROM user_role ur, roles r " //
                + " WHERE ur.role_id = r.id AND ur.user_id = ? ";

        Object[] params = new Object[]{userId};
        return this.getJdbcTemplate().queryForList(sql, params, String.class);
    }
}

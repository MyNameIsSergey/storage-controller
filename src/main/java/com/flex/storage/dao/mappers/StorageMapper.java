package com.flex.storage.dao.mappers;

import com.flex.storage.models.Storage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageMapper implements RowMapper<Storage> {

    @Override
    public Storage mapRow(ResultSet resultSet, int i) throws SQLException {
        Storage storage = new Storage();
        storage.setName(resultSet.getString("name"));
        storage.setId(resultSet.getInt("id"));
        return storage;
    }
}

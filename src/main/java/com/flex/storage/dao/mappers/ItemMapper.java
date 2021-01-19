package com.flex.storage.dao.mappers;

import com.flex.storage.models.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        Item item = new Item();

        item.setCount(resultSet.getInt("count"));
        item.setCurrent(resultSet.getInt("current"));
        item.setId(resultSet.getInt("id"));
        item.setMarking(resultSet.getString("marking"));
        item.setSection(resultSet.getString("section"));
        item.setType(resultSet.getString("type"));

        return item;
    }
}

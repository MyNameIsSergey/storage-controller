package com.flex.storage.dao;

import com.flex.storage.dao.mappers.ItemMapper;
import com.flex.storage.dao.mappers.StorageMapper;
import com.flex.storage.models.Item;
import com.flex.storage.models.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class StoragesDao extends JdbcDaoSupport {
    @Autowired
    public StoragesDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public Storage getStorageById(int id) {
        String findStorageNameSql = "SELECT name FROM storages WHERE id = ?;";
        String selectItemsSql = "SELECT * FROM items WHERE storage_id = ?;";

        String name = getJdbcTemplate().queryForObject(findStorageNameSql, String.class, id);
        List<Item> items = getJdbcTemplate().queryForList(selectItemsSql, Item.class, id);

        Storage storage = new Storage();
        storage.setId(id);
        storage.setName(name);
        storage.setItems(items);

        return storage;
    }

    public List<Storage> getStoragesOfUserWithoutItems(int userId) {
        String sql = "SELECT id, name FROM storages WHERE user_id = ?;";
        Object[] params = new Object[] { userId };
        return this.getJdbcTemplate().query(sql, new StorageMapper(), params);
    }

    public List<Item> loadItems(int storageId) {
        String sql = "SELECT * FROM items WHERE storage_id = ?;";
        Object[] params = new Object[] { storageId };
        return this.getJdbcTemplate().query(sql, new ItemMapper(), params);
    }

    public void saveStorage(Storage storage, int userId) {
        String sql = "INSERT INTO storages (name, user_id) VALUES (?,?);";
        this.getJdbcTemplate().update(sql, storage.getName(), userId);
    }

    public void saveItem(Item item, int storageId) {
        String sql = "INSERT INTO items (type,section,marking,count,current,storage_id) " +
                "VALUES (?,?,?,?,?,?);";
        Object[] params = {
                item.getType(),
                item.getSection(),
                item.getMarking(),
                item.getCount(),
                item.getCurrent(),
                storageId
        };
        this.getJdbcTemplate().update(sql, params);
    }
}

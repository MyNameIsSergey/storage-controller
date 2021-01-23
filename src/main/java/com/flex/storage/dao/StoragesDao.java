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
        String sql = "INSERT INTO storages (name, user_id) VALUES (?,?);" +
                "SELECT LAST_INSERT_ID();";
        Integer id = this.getJdbcTemplate().queryForObject(sql, Integer.class, storage.getName(), userId);
        storage.setId(id);
    }

    public void updateItem(Item item) {
        String sql = "UPDATE items SET type=?,section=?,marking=?,count=?,current=? " +
                "WHERE id = ?;";
        Object[] params = {
                item.getType(),
                item.getSection(),
                item.getMarking(),
                item.getCount(),
                item.getCurrent(),
                item.getId()
        };
        this.getJdbcTemplate().update(sql, params);
    }

    public void saveItem(Item item, int storageId) {
        String sql = "INSERT INTO items (type,section,marking,count,current,storage_id) " +
                "VALUES (?,?,?,?,?,?);" +
                "SELECT LAST_INSERT_ID();";
        Object[] params = {
                item.getType(),
                item.getSection(),
                item.getMarking(),
                item.getCount(),
                item.getCurrent(),
                storageId
        };
        Integer id = this.getJdbcTemplate().queryForObject(sql, Integer.class, params);
        item.setId(id);
    }
}

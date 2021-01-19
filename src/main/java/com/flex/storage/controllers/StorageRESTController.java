package com.flex.storage.controllers;

import com.flex.storage.dao.StoragesDao;
import com.flex.storage.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StorageRESTController {
    @Autowired
    StoragesDao dao;

    @GetMapping("/item/all")
    public List<Item> getItemsOfStorage(int storageId) {
        return dao.loadItems(storageId);
    }

    @PostMapping("/item")
    private ResponseEntity<Item> addItem(Item item, int storageId) {
        dao.saveItem(item, storageId);
        return ResponseEntity.ok(item);
    }
}

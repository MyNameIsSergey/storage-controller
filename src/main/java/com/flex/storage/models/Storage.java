package com.flex.storage.models;

import java.io.Serializable;
import java.util.List;

public class Storage implements Serializable {
    private int id;

    private String name;

    private List<Item> items;

    public Storage() {
    }

    public Storage(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

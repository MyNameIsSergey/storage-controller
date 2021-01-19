package com.flex.storage.controllers;

import com.flex.storage.dao.StoragesDao;
import com.flex.storage.models.ExtendedUser;
import com.flex.storage.models.Item;
import com.flex.storage.models.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StorageController {

    @Autowired
    StoragesDao dao;

    @GetMapping("/storages")
    public String getItemsInStorage(Model model) {
        ExtendedUser details = (ExtendedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Storage> storages = dao.getStoragesOfUserWithoutItems(details.getId());
        model.addAttribute("storages", storages);
        return "storages";
    }

    @PostMapping("/create")
    public String createStorage(String storageName) {
        int id = ((ExtendedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dao.saveStorage(new Storage(storageName), id);
        return "redirect:storages";
    }
}

package com.flex.storage.services;

import com.flex.storage.dao.RolesDao;
import com.flex.storage.dao.UsersDao;
import com.flex.storage.exceptions.UserAlreadyExistException;
import com.flex.storage.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UsersDao usersDao;

    @Autowired
    RolesDao rolesDao;

    public void addUser(UserModel user) throws UserAlreadyExistException {
        if (usersDao.findUserAccount(user.getUsername()) != null) {
            throw new UserAlreadyExistException(user.getUsername());
        }
        usersDao.addUser(user);
        rolesDao.addDefaultRoleToUser(user.getId());
    }
}

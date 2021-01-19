package com.flex.storage.services;

import com.flex.storage.dao.RolesDao;
import com.flex.storage.dao.UsersDao;
import com.flex.storage.models.ExtendedUser;
import com.flex.storage.models.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersDao userDAO;

    private final RolesDao roleDAO;

    public UserDetailsServiceImpl(UsersDao appUserDAO, RolesDao appRoleDAO) {
        this.userDAO = appUserDAO;
        this.roleDAO = appRoleDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel user = this.userDAO.findUserAccount(userName);

        if (user == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
        return createUserDetails(user);
    }

    public UserDetails loadUserById(int id) {
        UserModel user = userDAO.findUserAccountById(id);
        return createUserDetails(user);
    }

    UserDetails createUserDetails(UserModel user) {
        List<String> rolesNames = roleDAO.getUserRoleNames(user.getId());
        Collection<GrantedAuthority> roles = rolesNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        ExtendedUser details = new ExtendedUser(user.getUsername(), user.getEncryptedPassword(), roles);
        details.setId(user.getId());
        return details;

    }
}

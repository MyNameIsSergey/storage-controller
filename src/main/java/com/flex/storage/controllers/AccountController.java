package com.flex.storage.controllers;

import com.flex.storage.exceptions.UserAlreadyExistException;
import com.flex.storage.models.UserModel;
import com.flex.storage.services.UserService;
import com.flex.storage.viewModels.RegisterVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    String register(@ModelAttribute("userForm") RegisterVM form, Model model) {

        boolean passwordError = form.getPassword() == null || form.getPassword().equals("");
        boolean usernameError = form.getUsername() == null || form.getUsername().equals("");
        boolean passwordNotEqual = !form.getPassword().equals(form.getConfirmPassword());

        if(passwordError || usernameError || passwordNotEqual) {
            model.addAttribute("passwordError", passwordError);
            model.addAttribute("usernameError", usernameError);
            model.addAttribute("passwordNotEqual", passwordNotEqual);
            return "register";
        }

        try {
            userService.addUser(createUser(form));
        } catch (UserAlreadyExistException e) {
            model.addAttribute("userAlreadyExist", true);
            return "register";
        }

        return "login";
    }

    private UserModel createUser(RegisterVM form) {
        UserModel user = new UserModel();
        user.setEnabled(true);
        user.setEncryptedPassword(passwordEncoder.encode(form.getPassword()));
        user.setUsername(form.getUsername());
        return user;
    }
}

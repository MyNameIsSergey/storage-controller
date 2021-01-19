package com.flex.storage.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String username) {
        super("User: " + username + " already exist");
    }
}

package com.nike.nike.service;

import java.util.List;

import com.nike.nike.model.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    void assignRolesToUser(User user, List<Long> roleIds);
    List<User> searchUsers(String keyword);
}

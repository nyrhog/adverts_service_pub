package com.project.service;

import com.project.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User register(User user);

    List<User> getAll();

    Optional<User> findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    User registerAdminUser(User user);

    User getUserById(Long id);

    void sendMessageWithCode(String username);

    void restorePassword(String username, int code, String newPassword);
}

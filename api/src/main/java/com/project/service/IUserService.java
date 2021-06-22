package com.project.service;

import com.project.entity.User;
import java.util.Optional;

public interface IUserService {

    User register(User user);

    Optional<User> findByUsername(String username);

    User registerAdminUser(User user);

    void sendMessageWithCode(String username);

    void restorePassword(String username, int code, String newPassword);
}

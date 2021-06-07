package com.project.service;

import com.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    void register() {

        User user = new User();

        userService.register(user);

        List<User> users = userService.getAll();
        System.out.println(users);
    }

    @Test
    void getAll() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void registerAdminUser() {
    }

    @Test
    void sendMessageWithCode() {
    }

    @Test
    void saveRestoreCode() {
    }

    @Test
    void restorePassword() {
    }

    @Test
    void getUserById() {
    }
}
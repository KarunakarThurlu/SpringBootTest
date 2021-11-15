package com.app.controller;

import com.app.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class UserControllerTest {

    @MockBean
    private UserController userController;

    @Test
    void login() {

    }

    @Test
    void saveUser() {
        User user = new User();


    }

    @Test
    void getUserById() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void searchUser() {
    }
}
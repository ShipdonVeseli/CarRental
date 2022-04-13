package com.carrental.service;

import com.carrental.entity.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void createUserTest() {
        String username = "Idiot";
        String password = "password";

        User user = new User();
        user.setPassword(password);
        user.setUsername(username);

        try {
            User actualUser = userService.createNewUser(user);

            System.out.println(user);
            System.out.println(actualUser);
            assertEquals(user, actualUser);

            //resetDB
            userService.removeUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
package com.example.linebot.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void findUserIdByName() {
        List<String> ids = userService.findUserIdByName("Andyhuang");
        assertNotNull(ids);
    }
}
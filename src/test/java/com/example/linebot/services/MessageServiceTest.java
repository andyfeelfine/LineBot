package com.example.linebot.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Test
    void findAllByUserId() {
        List result = messageService.findAllByUserId("U71bf079d159071a02ad6e428b0859562");
        assertNotNull(result);
    }
}
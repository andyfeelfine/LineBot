package com.example.linebot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.GetFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LineApiTest {
    @Autowired
    LineApi lineApi;
    private final Logger log = LoggerFactory.getLogger(LineApiTest.class);
    @Test
    void getUserProfile() throws ExecutionException, InterruptedException {
        UserProfileResponse profile = lineApi.getUserProfile("U71bf079d159071a02ad6e428b0859562");
        assertEquals("Andyhuang",profile.getDisplayName());
    }

    @Test
    void getFollowers() throws ExecutionException, InterruptedException {
        GetFollowersResponse getFollowersResponse   = lineApi.getFollowers();
        List<String> ids = getFollowersResponse.getUserIds();
        assertNotNull(ids);
    }

    @Test
    void getNumberOfFollowers() throws ExecutionException, InterruptedException {
        String date="20230101";
        GetNumberOfFollowersResponse getNumberOfFollowersResponse = lineApi.getNumberOfFollowers(date);
        assertNotNull(getNumberOfFollowersResponse);
    }

    @Test()
    void replyMessage() {
        Assertions.assertThrows(ExecutionException.class, () -> {
            ReplyMessage replyMessage=new ReplyMessage("ca199ae5462f49bb93c4b2634092ca80",new TextMessage("test reply"));
            BotApiResponse result = lineApi.replyMessage(replyMessage);
        });
    }

    @Test
    void pushMessageToUser() throws ExecutionException, InterruptedException {
        BotApiResponse botApiResponse = lineApi.pushMessageToUser("U71bf079d159071a02ad6e428b0859562", "testPushMessage");
        assertNotNull(botApiResponse);
    }
}
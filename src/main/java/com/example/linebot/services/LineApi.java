package com.example.linebot.services;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.request.GetFollowersRequest;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.GetFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.spring.boot.LineBotProperties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class LineApi {

    private final LineBotProperties lineBotProperties;
    LineMessagingClient client;

    public LineApi(LineBotProperties lineBotProperties) {
        this.lineBotProperties = lineBotProperties;
        client = LineMessagingClient.builder(lineBotProperties.getChannelToken()).build();
    }


    public UserProfileResponse getUserProfile(String userId) throws ExecutionException, InterruptedException {
        CompletableFuture<UserProfileResponse> profile = client
                .getProfile(userId);
        UserProfileResponse userProfile = profile.get();
        return userProfile;
    }

    public GetFollowersResponse getFollowers() throws ExecutionException, InterruptedException {
        GetFollowersResponse getFollowersResponse = client.getFollowers(GetFollowersRequest.builder().build()).get();
        return getFollowersResponse;
    }

    public BotApiResponse replyMessage(ReplyMessage replyMessage) throws ExecutionException, InterruptedException {
        BotApiResponse apiResponse = client.replyMessage(replyMessage).get();
        return apiResponse;
    }

    public GetNumberOfFollowersResponse getNumberOfFollowers(String date)
            throws ExecutionException, InterruptedException {
        CompletableFuture<GetNumberOfFollowersResponse> getNumberOfFollowersResponseCompletableFuture = client
                .getNumberOfFollowers(date);
        return getNumberOfFollowersResponseCompletableFuture.get();

    }

    public BotApiResponse pushMessageToUser(String to, String message) throws ExecutionException, InterruptedException {
        PushMessage pushMessage = new PushMessage(to, new TextMessage(message));
        return client.pushMessage(pushMessage).get();
    }

}

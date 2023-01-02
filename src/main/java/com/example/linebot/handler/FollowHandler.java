package com.example.linebot.handler;

import com.example.linebot.model.entity.UserProfileDao;
import com.example.linebot.model.repository.UserProflieRepository;
import com.example.linebot.services.LineApi;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LineMessageHandler
public class FollowHandler {

    private final Logger log = LoggerFactory.getLogger(FollowHandler.class);

    LineApi lineApi;
    UserProflieRepository userProflieRepository;

    public FollowHandler(LineApi lineApi, UserProflieRepository userProflieRepository) {
        this.lineApi = lineApi;
        this.userProflieRepository = userProflieRepository;
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
        String userId = event.getSource().getUserId();
        Instant updatetime = event.getTimestamp();
        try {
            UserProfileResponse userProfile = lineApi.getUserProfile(userId);
            UserProfileDao userProfileDao = new UserProfileDao(userProfile);
            userProfileDao.updateFollowStatus(true, updatetime);
            userProflieRepository.save(userProfileDao);
        } catch (InterruptedException | ExecutionException e) {
            log.error("follower save to db error:", userId);
        }
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        String userId = event.getSource().getUserId();
        Instant updatetime = event.getTimestamp();
        try {
            Optional<UserProfileDao> userProflieRepositoryById = userProflieRepository.findById(userId);
            UserProfileDao userProfileDao = userProflieRepositoryById.get();
            userProfileDao.updateFollowStatus(false, updatetime);
            userProflieRepository.save(userProfileDao);
        } catch (Exception e) {
            log.error("unfollow update to db error:", userId);
        }
    }

}

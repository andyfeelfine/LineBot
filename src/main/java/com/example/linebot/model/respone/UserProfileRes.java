package com.example.linebot.model.respone;

import com.example.linebot.model.entity.UserProfileDao;
import java.net.URI;

public class UserProfileRes {

    private String userId;
    private String displayName;
    private URI pictureUrl;
    private String statusMessage;

    public UserProfileRes() {
    }

    public UserProfileRes(String userId, String displayName, URI pictureUrl, String statusMessage) {
        this.userId = userId;
        this.displayName = displayName;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }

    public UserProfileRes(UserProfileDao userProfileDao) {
        this.userId = userProfileDao.getUserId();
        this.displayName = userProfileDao.getDisplayName();
        this.pictureUrl = userProfileDao.getPictureUrl();
        this.statusMessage = userProfileDao.getStatusMessage();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public URI getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(URI pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}

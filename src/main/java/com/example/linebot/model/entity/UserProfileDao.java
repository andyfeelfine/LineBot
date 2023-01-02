package com.example.linebot.model.entity;

import com.linecorp.bot.model.profile.UserProfileResponse;
import java.net.URI;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserProfile")
public class UserProfileDao {

    @Id
    private String userId;
    private String displayName;
    private URI pictureUrl;
    private String statusMessage;
    private Boolean isFollower;
    private Instant updateTimestamp;

    public UserProfileDao() {
    }

    public UserProfileDao(UserProfileResponse userProfileResponse) {
        this.userId = userProfileResponse.getUserId();
        this.displayName = userProfileResponse.getDisplayName();
        this.pictureUrl = userProfileResponse.getPictureUrl();
        this.statusMessage = userProfileResponse.getStatusMessage();
    }

    public void updateFollowStatus(Boolean isFollower, Instant updateTimestamp) {
        this.isFollower = isFollower;
        this.updateTimestamp = updateTimestamp;
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

    public Boolean getFollower() {
        return isFollower;
    }

    public void setFollower(Boolean follower) {
        isFollower = follower;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}

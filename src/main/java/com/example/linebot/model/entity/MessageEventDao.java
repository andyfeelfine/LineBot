package com.example.linebot.model.entity;

import com.linecorp.bot.model.event.DeliveryContext;
import com.linecorp.bot.model.event.EventMode;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.source.Source;
import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("MessageEvent")
public class MessageEventDao<T> {

    String replyToken;

    Source source;

    T message;

    Instant timestamp;

    EventMode mode;

    String webhookEventId;

    DeliveryContext deliveryContext;

    public MessageEventDao() {
    }

    public MessageEventDao(MessageEvent event) {
        this.replyToken = event.getReplyToken();
        this.source = event.getSource();
        this.message = (T) event.getMessage();
        this.timestamp = event.getTimestamp();
        this.mode = event.getMode();
        this.webhookEventId = event.getWebhookEventId();
        this.deliveryContext = event.getDeliveryContext();
    }

    public String getReplyToken() {
        return replyToken;
    }

    public void setReplyToken(String replyToken) {
        this.replyToken = replyToken;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public EventMode getMode() {
        return mode;
    }

    public void setMode(EventMode mode) {
        this.mode = mode;
    }

    public String getWebhookEventId() {
        return webhookEventId;
    }

    public void setWebhookEventId(String webhookEventId) {
        this.webhookEventId = webhookEventId;
    }

    public DeliveryContext getDeliveryContext() {
        return deliveryContext;
    }

    public void setDeliveryContext(DeliveryContext deliveryContext) {
        this.deliveryContext = deliveryContext;
    }
}

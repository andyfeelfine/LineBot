package com.example.linebot.handler;

import static java.util.Collections.singletonList;

import com.example.linebot.LinebotApplication;
import com.example.linebot.model.entity.MessageEventDao;
import com.example.linebot.model.entity.TextMessageEventDao;
import com.example.linebot.model.repository.MessageEventRepository;
import com.example.linebot.model.repository.TextMessageEventRepository;
import com.example.linebot.services.LineApi;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnknownEvent;
import com.linecorp.bot.model.event.VideoPlayCompleteEvent;
import com.linecorp.bot.model.event.message.FileMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.sender.Sender;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@LineMessageHandler
public class MessageHandler {

    private final Logger log = LoggerFactory.getLogger(LinebotApplication.class);

    TextMessageEventRepository textMessageEventRepository;
    LineApi lineApi;
    MessageEventRepository messageEventRepository;

    public MessageHandler(TextMessageEventRepository textMessageEventRepository,
            LineApi lineApi, MessageEventRepository messageEventRepository) {
        this.textMessageEventRepository = textMessageEventRepository;
        this.lineApi = lineApi;
        this.messageEventRepository = messageEventRepository;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("event: " + event);
        TextMessageContent message = event.getMessage();
        textMessageEventRepository.save(new TextMessageEventDao(event));
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        handleSticker(event.getReplyToken(), event.getMessage());
        messageEventRepository.save(new MessageEventDao<StickerMessageContent>(event));
        log.info(event.getReplyToken(), event.getMessage());
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        reply(event.getReplyToken(), new LocationMessage(
                locationMessage.getTitle(),
                locationMessage.getAddress(),
                locationMessage.getLatitude(),
                locationMessage.getLongitude()
        ));
        log.info(event.getReplyToken(), new LocationMessage(
                locationMessage.getTitle(),
                locationMessage.getAddress(),
                locationMessage.getLatitude(),
                locationMessage.getLongitude()
        ));
    }

    @EventMapping
    public void handleVideoPlayCompleteEvent(VideoPlayCompleteEvent event) throws IOException {
        log.info("Got video play complete: tracking id={}", event.getVideoPlayComplete().getTrackingId());
        this.replyText(event.getReplyToken(),
                "You played " + event.getVideoPlayComplete().getTrackingId());
    }

    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        this.reply(event.getReplyToken(),
                new TextMessage(String.format("Received '%s'(%d bytes)",
                        event.getMessage().getFileName(),
                        event.getMessage().getFileSize())));
    }

    @EventMapping
    public void handleUnknownEvent(UnknownEvent event) {
        log.info("Got an unknown event!!!!! : {}", event);
    }


    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken,
            @NonNull List<Message> messages,
            boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineApi
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled));
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        final String text = content.getText();

        log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text,
                content.getEmojis());
        switch (text) {
            case "profile": {
                log.info("Invoking 'profile' command: source:{}",
                        event.getSource());
                final String userId = event.getSource().getUserId();
                if (userId != null) {
                    UserProfileResponse userProfile = lineApi.getUserProfile(userId);
                    this.reply(
                            replyToken,
                            Arrays.asList(new TextMessage("(from group)"),
                                    new TextMessage(
                                            "Display name: " + userProfile.getDisplayName()),
                                    new ImageMessage(userProfile.getPictureUrl(),
                                            userProfile.getPictureUrl()))
                    );
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "confirm": {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        new MessageAction("Yes", "Yes!"),
                        new MessageAction("No", "No!")
                );
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "buttons": {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        "My button sample",
                        "Hello, my button",
                        Arrays.asList(
                                new URIAction("Go to line.me",
                                        URI.create("https://line.me"), null),
                                new PostbackAction("Say hello1",
                                        "hello こんにちは"),
                                new PostbackAction("言 hello2",
                                        "hello こんにちは",
                                        "hello こんにちは"),
                                new MessageAction("Say message",
                                        "Rice=米")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "carousel": {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new PostbackAction("Say hello1",
                                                "hello こんにちは")
                                )),
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは"),
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは"),
                                        new MessageAction("Say message",
                                                "Rice=米")
                                )),
                                new CarouselColumn(imageUrl, "Datetime Picker",
                                        "Please select a date, time or datetime", Arrays.asList(
                                        DatetimePickerAction.OfLocalDatetime
                                                .builder()
                                                .label("Datetime")
                                                .data("action=sel")
                                                .initial(LocalDateTime.parse("2017-06-18T06:15"))
                                                .min(LocalDateTime.parse("1900-01-01T00:00"))
                                                .max(LocalDateTime.parse("2100-12-31T23:59"))
                                                .build(),
                                        DatetimePickerAction.OfLocalDate
                                                .builder()
                                                .label("Date")
                                                .data("action=sel&only=date")
                                                .initial(LocalDate.parse("2017-06-18"))
                                                .min(LocalDate.parse("1900-01-01"))
                                                .max(LocalDate.parse("2100-12-31"))
                                                .build(),
                                        DatetimePickerAction.OfLocalTime
                                                .builder()
                                                .label("Time")
                                                .data("action=sel&only=time")
                                                .initial(LocalTime.parse("06:15"))
                                                .min(LocalTime.parse("00:00"))
                                                .max(LocalTime.parse("23:59"))
                                                .build()
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "no_notify":
                this.reply(replyToken,
                        singletonList(new TextMessage("This message is send without a push notification")),
                        true);
                break;
            case "redelivery":
                this.reply(replyToken,
                        singletonList(new TextMessage("webhookEventId=" + event.getWebhookEventId()
                                + " deliveryContext.isRedelivery=" + event.getDeliveryContext().getIsRedelivery())
                        ));
                break;
            case "icon":
                this.reply(replyToken,
                        TextMessage.builder()
                                .text("Hello, I'm cat! Meow~")
                                .sender(Sender.builder()
                                        .name("Cat")
                                        .iconUrl(createUri("/static/icon/cat.png"))
                                        .build())
                                .build());
                break;
            default:
                log.info("Returns echo message {}: {}", replyToken, text);
                this.replyText(
                        replyToken,
                        text
                );
                break;
        }
    }

    private void handleSticker(String replyToken, StickerMessageContent content) {
        reply(replyToken, new StickerMessage(
                content.getPackageId(), content.getStickerId())
        );
    }

    private static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")
                .path(path).build()
                .toUri();
    }

}

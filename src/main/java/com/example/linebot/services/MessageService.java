package com.example.linebot.services;

import com.example.linebot.model.entity.TextMessageEventDao;
import com.example.linebot.model.repository.TextMessageEventRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    TextMessageEventRepository textMessageEventRepository;

    public MessageService(TextMessageEventRepository textMessageEventRepository) {
        this.textMessageEventRepository = textMessageEventRepository;
    }

    public List findAllByUserId(String userId) {
        List<TextMessageEventDao> textMessageEventDaos = textMessageEventRepository.findAllByUserId(userId);
        List<String> texts = textMessageEventDaos.stream()
                .sorted(Comparator.comparing(TextMessageEventDao::getTimestamp))
                .map(e -> e.getMessage().getText()).collect(
                        Collectors.toList());
        return texts;
    }


}

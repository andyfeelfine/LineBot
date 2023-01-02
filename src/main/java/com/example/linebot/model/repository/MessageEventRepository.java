package com.example.linebot.model.repository;

import com.example.linebot.model.entity.MessageEventDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageEventRepository extends MongoRepository<MessageEventDao, String> {

}

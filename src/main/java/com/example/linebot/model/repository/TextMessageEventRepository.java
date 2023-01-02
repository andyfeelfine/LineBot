package com.example.linebot.model.repository;


import com.example.linebot.model.entity.TextMessageEventDao;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TextMessageEventRepository extends MongoRepository<TextMessageEventDao, String> {

    @Query(value = "{ 'source.userId' : ?0 }", sort = "{ age : -1 }")
    List<TextMessageEventDao> findAllByUserId(String userId);
}

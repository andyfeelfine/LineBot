package com.example.linebot.model.repository;

import com.example.linebot.model.entity.UserProfileDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProflieRepository extends MongoRepository<UserProfileDao, String> {

    Iterable<UserProfileDao> findAllByDisplayNameContains(String displayName);

}

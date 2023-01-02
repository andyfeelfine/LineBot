package com.example.linebot.services;

import com.example.linebot.model.entity.UserProfileDao;
import com.example.linebot.model.repository.UserProflieRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserProflieRepository userProflieRepository;

    public UserService(UserProflieRepository userProflieRepository) {
        this.userProflieRepository = userProflieRepository;
    }

    public List<String> findUserIdByName(String displayName) {
        Iterable<UserProfileDao> userProfileDaoIterable = userProflieRepository
                .findAllByDisplayNameContains(displayName);
        List<String> ids =
                StreamSupport.stream(userProfileDaoIterable.spliterator(), false)
                        .map(UserProfileDao::getUserId)
                        .collect(Collectors.toList());
        return ids;
    }
}

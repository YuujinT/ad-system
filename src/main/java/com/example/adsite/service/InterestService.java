package com.example.adsite.service;

import com.example.adsite.dao.UserTagDao;

import java.util.List;
import java.util.Optional;

public class InterestService {
    private final UserTagDao userTagDao;

    public InterestService(UserTagDao userTagDao) {
        this.userTagDao = userTagDao;
    }

    public void collectInterest(String userId, List<String> tags) {
        userTagDao.incrementTags(userId, tags);
    }

    public Optional<String> findDominantTag(String userId) {
        return userTagDao.findDominantTag(userId);
    }
}

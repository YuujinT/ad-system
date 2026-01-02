package com.example.adsite.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserTagDao {
    List<String> supportedTags();

    void incrementTags(String userId, List<String> tags);

    Map<String, Integer> findTagCounts(String userId);

    Optional<String> findDominantTag(String userId);
}

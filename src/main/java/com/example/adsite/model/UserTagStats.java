package com.example.adsite.model;

import java.util.Map;

public class UserTagStats {
    private final String userId;
    private final Map<String, Integer> tagCounts;

    public UserTagStats(String userId, Map<String, Integer> tagCounts) {
        this.userId = userId;
        this.tagCounts = tagCounts;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getTagCounts() {
        return tagCounts;
    }
}


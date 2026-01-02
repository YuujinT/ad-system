package com.example.adsite.dao.impl;

import com.example.adsite.dao.UserTagDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserTagDaoImpl implements UserTagDao {
    private static final List<String> TAG_COLUMNS = List.of(
            "technology",
            "gaming",
            "travel",
            "sports",
            "food"
    );

    private final DataSource dataSource;

    public UserTagDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> supportedTags() {
        return TAG_COLUMNS;
    }

    @Override
    public void incrementTags(String userId, List<String> tags) {
        if (userId == null || userId.isBlank() || tags == null || tags.isEmpty()) {
            return;
        }
        Map<String, Long> increments = tags.stream()
                .filter(tag -> TAG_COLUMNS.contains(tag))
                .collect(Collectors.groupingBy(tag -> tag, LinkedHashMap::new, Collectors.counting()));
        if (increments.isEmpty()) {
            return;
        }
        ensureUserExists(userId);
        for (Map.Entry<String, Long> entry : increments.entrySet()) {
            String sql = "UPDATE user_tags SET " + entry.getKey() + " = " + entry.getKey() + " + ? WHERE id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, entry.getValue());
                statement.setString(2, userId);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Failed to increment tag stats", e);
            }
        }
    }

    @Override
    public Map<String, Integer> findTagCounts(String userId) {
        if (userId == null || userId.isBlank()) {
            return Collections.emptyMap();
        }
        String sql = "SELECT " + String.join(",", TAG_COLUMNS) + " FROM user_tags WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Collections.emptyMap();
                }
                Map<String, Integer> counts = new LinkedHashMap<>();
                for (String column : TAG_COLUMNS) {
                    counts.put(column, rs.getInt(column));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to fetch tag counts", e);
        }
    }

    @Override
    public Optional<String> findDominantTag(String userId) {
        Map<String, Integer> counts = findTagCounts(userId);
        if (counts.isEmpty()) {
            return Optional.empty();
        }
        int max = counts.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        if (max <= 0) {
            return Optional.empty();
        }
        List<String> topTags = new ArrayList<>();
        for (String column : TAG_COLUMNS) {
            if (counts.getOrDefault(column, 0) == max) {
                topTags.add(column);
            }
        }
        if (topTags.isEmpty()) {
            return Optional.empty();
        }
        int pick = java.util.concurrent.ThreadLocalRandom.current().nextInt(topTags.size());
        return Optional.of(topTags.get(pick));
    }

    private void ensureUserExists(String userId) {
        List<String> placeholders = new ArrayList<>(TAG_COLUMNS.size());
        TAG_COLUMNS.forEach(column -> placeholders.add("0"));
        String sql = "INSERT INTO user_tags (id, " + String.join(",", TAG_COLUMNS) + ") VALUES ("
                + placeholders(userId, TAG_COLUMNS.size()) + ") ON DUPLICATE KEY UPDATE id = id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to initialize user row", e);
        }
    }

    private String placeholders(String userId, int tagCount) {
        StringBuilder builder = new StringBuilder("?");
        for (int i = 0; i < tagCount; i++) {
            builder.append(",0");
        }
        return builder.toString();
    }
}


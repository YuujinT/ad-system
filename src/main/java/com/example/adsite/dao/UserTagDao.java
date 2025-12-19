package com.example.adsite.dao;

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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DAO for manipulating user_tags table and resolving the dominant interest tag per user.
 */
public class UserTagDao {
    private static final List<String> TAG_COLUMNS = List.of(
            "technology",
            "gaming",
            "travel",
            "sports",
            "food"
    );

    private final DataSource dataSource;

    public UserTagDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> supportedTags() {
        return TAG_COLUMNS;
    }

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

    public Optional<String> findDominantTag(String userId) {
        Map<String, Integer> counts = findTagCounts(userId);
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey);
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

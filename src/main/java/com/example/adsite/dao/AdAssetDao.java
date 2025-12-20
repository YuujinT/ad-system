package com.example.adsite.dao;

import com.example.adsite.model.AdAsset;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdAssetDao {
    private final DataSource dataSource;

    public AdAssetDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<AdAsset> findTopAdForTag(String tag, AdAsset.Format format) {
        String sql = "SELECT * FROM ad_assets WHERE interest_tag = ? AND content_type LIKE ? ORDER BY id ASC LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tag);
            statement.setString(2, format == AdAsset.Format.VIDEO ? "video%" : "image%");
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to fetch ad asset", e);
        }
    }

    public Optional<AdAsset> findAnyAd(AdAsset.Format format) {
        String sql = "SELECT * FROM ad_assets WHERE content_type LIKE ? ORDER BY RAND() LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, format == AdAsset.Format.VIDEO ? "video%" : "image%");
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to fetch fallback ad", e);
        }
    }

    public List<AdAsset> findByOwner(String ownerId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ad_assets WHERE owner_id = ? ORDER BY id DESC")) {
            statement.setString(1, ownerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<AdAsset> assets = new ArrayList<>();
                while (rs.next()) {
                    assets.add(mapRow(rs));
                }
                return assets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to query assets", e);
        }
    }

    public long insert(AdAsset asset) {
        String sql = "INSERT INTO ad_assets (owner_id, file_name, content_type, interest_tag) VALUES (?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, asset.getOwnerId());
            statement.setString(2, asset.getFileName());
            statement.setString(3, asset.getContentType());
            statement.setString(4, asset.getInterestTag());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Missing generated key for asset");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to insert ad asset", e);
        }
    }

    public void delete(long assetId, String ownerId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ad_assets WHERE id = ? AND owner_id = ?")) {
            statement.setLong(1, assetId);
            statement.setString(2, ownerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete ad asset", e);
        }
    }

    public Optional<AdAsset> findById(long assetId, String ownerId) {
        String sql = "SELECT * FROM ad_assets WHERE id = ? AND owner_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, assetId);
            statement.setString(2, ownerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to load asset", e);
        }
    }

    private AdAsset mapRow(ResultSet rs) throws SQLException {
        return new AdAsset(
                rs.getLong("id"),
                rs.getString("owner_id"),
                rs.getString("file_name"),
                rs.getString("content_type"),
                rs.getString("interest_tag")
        );
    }
}

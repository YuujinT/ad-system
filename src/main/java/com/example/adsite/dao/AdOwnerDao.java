package com.example.adsite.dao;

import com.example.adsite.model.AdOwner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdOwnerDao {
    private final DataSource dataSource;

    public AdOwnerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<AdOwner> findByUsername(String username) {
        String sql = "SELECT AdAccount, Password FROM ad_owner WHERE AdAccount = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to query ad account", e);
        }
    }

    private AdOwner mapRow(ResultSet rs) throws SQLException {
        return new AdOwner(
                rs.getString("AdAccount"),
                rs.getString("Password")
        );
    }
}

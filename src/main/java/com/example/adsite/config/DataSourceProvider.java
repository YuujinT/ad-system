package com.example.adsite.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides a singleton HikariCP datasource configured from application.properties.
 */
public final class DataSourceProvider {
    private static final HikariDataSource DATA_SOURCE = buildDataSource();

    private DataSourceProvider() {
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    private static HikariDataSource buildDataSource() {
        Properties properties = new Properties();
        try (InputStream in = DataSourceProvider.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException("Missing application.properties on classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load database configuration", e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.username"));
        config.setPassword(properties.getProperty("db.password"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.pool.size", "5")));
        config.setPoolName("ad-site-pool");
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }
}

CREATE DATABASE IF NOT EXISTS ad_site DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ad_site;

DROP TABLE IF EXISTS ad_assets;
DROP TABLE IF EXISTS ad_owners;
DROP TABLE IF EXISTS ad_accounts;

CREATE TABLE IF NOT EXISTS ad_owner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    AdAccount VARCHAR(64) NOT NULL UNIQUE,
    Password VARCHAR(128) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ad_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id VARCHAR(64) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(128) NOT NULL,
    interest_tag VARCHAR(32) NOT NULL,
    CONSTRAINT fk_asset_owner FOREIGN KEY (owner_id) REFERENCES ad_owner(AdAccount)
);

DROP TABLE IF EXISTS user_tags;
CREATE TABLE IF NOT EXISTS user_tags (
    id VARCHAR(64) PRIMARY KEY,
    technology INT DEFAULT 0,
    gaming INT DEFAULT 0,
    travel INT DEFAULT 0,
    sports INT DEFAULT 0,
    food INT DEFAULT 0
);

INSERT INTO ad_owner (AdAccount, Password)
VALUES ('demo_owner', 'demo123')
ON DUPLICATE KEY UPDATE Password = VALUES(Password);

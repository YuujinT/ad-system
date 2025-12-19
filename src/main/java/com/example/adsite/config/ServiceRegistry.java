package com.example.adsite.config;

import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.dao.AdOwnerDao;
import com.example.adsite.dao.UserTagDao;
import com.example.adsite.service.AdService;
import com.example.adsite.service.InterestService;

/**
 * Lazily exposes singletons for application services.
 */
public final class ServiceRegistry {
    private static final UserTagDao USER_TAG_DAO = new UserTagDao(DataSourceProvider.getDataSource());
    private static final InterestService INTEREST_SERVICE = new InterestService(USER_TAG_DAO);
    private static final AdAssetDao AD_ASSET_DAO = new AdAssetDao(DataSourceProvider.getDataSource());
    private static final AdOwnerDao AD_OWNER_DAO = new AdOwnerDao(DataSourceProvider.getDataSource());
    private static final AdService AD_SERVICE = new AdService(INTEREST_SERVICE, AD_ASSET_DAO);

    private ServiceRegistry() {
    }

    public static InterestService interestService() {
        return INTEREST_SERVICE;
    }

    public static AdService adService() {
        return AD_SERVICE;
    }

    public static AdAssetDao adAssetDao() {
        return AD_ASSET_DAO;
    }

    public static AdOwnerDao ownerDao() {
        return AD_OWNER_DAO;
    }

    public static UserTagDao userTagDao() {
        return USER_TAG_DAO;
    }
}

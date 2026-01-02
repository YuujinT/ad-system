package com.example.adsite.config;

import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.dao.AdOwnerDao;
import com.example.adsite.dao.UserTagDao;
import com.example.adsite.dao.impl.AdAssetDaoImpl;
import com.example.adsite.dao.impl.AdOwnerDaoImpl;
import com.example.adsite.dao.impl.UserTagDaoImpl;
import com.example.adsite.service.AdService;
import com.example.adsite.service.AssetService;
import com.example.adsite.service.InterestService;

/**
 * Lazily exposes singletons for application services.
 */
public final class ServiceRegistry {
    private static final UserTagDao USER_TAG_DAO = new UserTagDaoImpl(DataSourceProvider.getDataSource());
    private static final InterestService INTEREST_SERVICE = new InterestService(USER_TAG_DAO);
    private static final AdAssetDao AD_ASSET_DAO = new AdAssetDaoImpl(DataSourceProvider.getDataSource());
    private static final AdOwnerDao AD_OWNER_DAO = new AdOwnerDaoImpl(DataSourceProvider.getDataSource());
    private static final AdService AD_SERVICE = new AdService(INTEREST_SERVICE, AD_ASSET_DAO);
    private static final AssetService ASSET_SERVICE = new AssetService(AD_ASSET_DAO);

    private ServiceRegistry() {
    }

    public static InterestService interestService() {
        return INTEREST_SERVICE;
    }

    public static AdService adService() {
        return AD_SERVICE;
    }

    public static AssetService assetService() {
        return ASSET_SERVICE;
    }

    public static AdOwnerDao ownerDao() {
        return AD_OWNER_DAO;
    }

    public static UserTagDao userTagDao() {
        return USER_TAG_DAO;
    }
}

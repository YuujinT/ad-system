package com.example.adsite.dao;

import com.example.adsite.model.AdAsset;

import java.util.List;
import java.util.Optional;

public interface AdAssetDao {
    Optional<AdAsset> findRandomAdForTag(String tag, AdAsset.Format format);

    Optional<AdAsset> findAnyAd(AdAsset.Format format);

    List<AdAsset> findByOwner(String ownerId);

    long insert(AdAsset asset);

    void delete(long assetId, String ownerId);

    Optional<AdAsset> findById(long assetId, String ownerId);
}

package com.example.adsite.service;

import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.model.AdAsset;

import java.util.Map;
import java.util.Optional;

public class AdService {
    private final InterestService interestService;
    private final AdAssetDao adAssetDao;

    public AdService(InterestService interestService, AdAssetDao adAssetDao) {
        this.interestService = interestService;
        this.adAssetDao = adAssetDao;
    }

    public Optional<AdAsset> pickAdForUser(String userId, AdAsset.Format format) {
        Optional<String> tagOpt = interestService.findDominantTag(userId);
        if (tagOpt.isPresent()) {
            Optional<AdAsset> tagAd = adAssetDao.findTopAdForTag(tagOpt.get(), format);
            if (tagAd.isPresent()) {
                return tagAd;
            }
        }
        return adAssetDao.findAnyAd(format);
    }

    public Map<String, Integer> loadStats(String userId) {
        return interestService.findTagCounts(userId);
    }
}


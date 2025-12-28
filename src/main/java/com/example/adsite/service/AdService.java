package com.example.adsite.service;

import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.model.AdAsset;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
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

    public AdAsset.Format parseFormat(String contentTypeParam) {
        if (contentTypeParam == null) {
            return AdAsset.Format.UNKNOWN;
        }
        String lower = contentTypeParam.toLowerCase(Locale.ROOT);
        if (lower.startsWith("video")) {
            return AdAsset.Format.VIDEO;
        }
        if (lower.startsWith("image")) {
            return AdAsset.Format.IMAGE;
        }
        return AdAsset.Format.UNKNOWN;
    }

    public Optional<Path> resolveAssetFile(AdAsset ad, String uploadDir) {
        if (uploadDir == null || uploadDir.isBlank()) {
            return Optional.empty();
        }
        Path file = Path.of(uploadDir, ad.getFileName());
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        return Optional.of(file);
    }

    public String detectMime(Path file, AdAsset ad) throws java.io.IOException {
        String mime = Files.probeContentType(file);
        return mime != null ? mime : ad.getContentType();
    }
}

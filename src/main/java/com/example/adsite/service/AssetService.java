package com.example.adsite.service;

import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.model.AdAsset;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class AssetService {
    private final AdAssetDao adAssetDao;

    public AssetService(AdAssetDao adAssetDao) {
        this.adAssetDao = adAssetDao;
    }

    public String generateStoredName(String submittedFileName) {
        String cleanName = submittedFileName == null ? "" : Path.of(submittedFileName).getFileName().toString();
        String ext = "";
        int dot = cleanName.lastIndexOf('.');
        if (dot > -1) {
            ext = cleanName.substring(dot);
        }
        return UUID.randomUUID() + ext;
    }

    public String resolveUploadDir(ServletContext ctx) throws ServletException {
        String uploadDir = ctx.getInitParameter("upload.dir");
        if (uploadDir == null || uploadDir.isBlank()) {
            uploadDir = ctx.getRealPath("/uploads");
        }
        if (uploadDir == null || uploadDir.isBlank()) {
            throw new ServletException("Uploads path not configured");
        }
        return uploadDir;
    }

    public void savePartToDisk(Part part, String uploadDir, String storedName) throws IOException {
        Files.createDirectories(Path.of(uploadDir));
        Path target = Path.of(uploadDir, storedName);
        part.write(target.toString());
    }

    public void insertAsset(AdAsset asset) {
        adAssetDao.insert(asset);
    }

    public void deleteAsset(String idParam, String owner, ServletContext ctx) {
        if (idParam == null || idParam.isBlank()) {
            return;
        }
        try {
            long assetId = Long.parseLong(idParam);
            adAssetDao.findById(assetId, owner).ifPresent(asset -> {
                String uploadDir = ctx.getInitParameter("upload.dir");
                if (uploadDir == null || uploadDir.isBlank()) {
                    uploadDir = ctx.getRealPath("/uploads");
                }
                if (uploadDir != null && !uploadDir.isBlank()) {
                    Path filePath = Path.of(uploadDir, asset.getFileName());
                    try {
                        Files.deleteIfExists(filePath);
                    } catch (IOException ignored) {
                        // ignore failure to delete file, still remove DB record
                    }
                }
                adAssetDao.delete(assetId, owner);
            });
        } catch (NumberFormatException ignored) {
            // ignore invalid id
        }
    }

    public List<AdAsset> findByOwner(String owner) {
        return adAssetDao.findByOwner(owner);
    }
}

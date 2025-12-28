package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.model.AdAsset;
import com.example.adsite.service.AdService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Unified ad delivery servlet. Accepts GET with userId and contentType (video/image).
 * Responds with the binary stream of the selected asset.
 */
public class AdApiServlet extends HttpServlet {
    private AdService adService;

    @Override
    public void init() throws ServletException {
        adService = ServiceRegistry.adService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("id");
        String contentTypeParam = req.getParameter("contentType");
        if (userId == null || userId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing id");
            return;
        }
        if (contentTypeParam == null || contentTypeParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing contentType");
            return;
        }
        AdAsset.Format format = adService.parseFormat(contentTypeParam);
        if (format == AdAsset.Format.UNKNOWN) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "unsupported contentType");
            return;
        }
        Optional<AdAsset> adOpt = adService.pickAdForUser(userId, format);
        if (adOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no ad available");
            return;
        }
        AdAsset ad = adOpt.get();
        String assetPath = req.getServletContext().getInitParameter("upload.dir");
        if (assetPath == null || assetPath.isBlank()) {
            assetPath = req.getServletContext().getRealPath("/uploads");
        }
        Optional<Path> fileOpt = adService.resolveAssetFile(ad, assetPath);
        if (fileOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "asset file missing");
            return;
        }
        Path file = fileOpt.get();
        String mime = adService.detectMime(file, ad);
        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + ad.getFileName() + "\"");
        resp.setContentLengthLong(java.nio.file.Files.size(file));
        try (OutputStream os = resp.getOutputStream()) {
            java.nio.file.Files.copy(file, os);
        }
    }
}

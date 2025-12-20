package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.model.AdAsset;
import com.example.adsite.service.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Optional;

/**
 * Unified ad delivery servlet. Accepts GET with userId and contentType (video/image).
 * Responds with the binary stream of the selected asset.
 */
public class AdApiServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
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
        AdAsset.Format format = resolveFormat(contentTypeParam);
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
        // Stream file content via AssetFileServlet path to respect configured upload dir
        String assetPath = req.getServletContext().getInitParameter("upload.dir");
        if (assetPath == null || assetPath.isBlank()) {
            assetPath = req.getServletContext().getRealPath("/uploads");
        }
        java.nio.file.Path file = java.nio.file.Path.of(assetPath, ad.getFileName());
        if (!java.nio.file.Files.exists(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "asset file missing");
            return;
        }
        String mime = java.nio.file.Files.probeContentType(file);
        if (mime == null) {
            mime = ad.getContentType();
        }
        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + ad.getFileName() + "\"");
        resp.setContentLengthLong(java.nio.file.Files.size(file));
        try (OutputStream os = resp.getOutputStream()) {
            java.nio.file.Files.copy(file, os);
        }
    }

    private AdAsset.Format resolveFormat(String contentTypeParam) {
        String lower = contentTypeParam.toLowerCase(Locale.ROOT);
        if (lower.startsWith("video")) {
            return AdAsset.Format.VIDEO;
        }
        if (lower.startsWith("image")) {
            return AdAsset.Format.IMAGE;
        }
        return AdAsset.Format.UNKNOWN;
    }
}


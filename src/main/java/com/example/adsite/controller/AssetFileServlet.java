package com.example.adsite.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Serves uploaded assets from the configured upload directory.
 */
public class AssetFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = Optional.ofNullable(req.getPathInfo())
                .map(p -> p.startsWith("/") ? p.substring(1) : p)
                .orElse("");
        if (filename.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String uploadDir = req.getServletContext().getInitParameter("upload.dir");
        if (uploadDir == null || uploadDir.isBlank()) {
            uploadDir = req.getServletContext().getRealPath("/uploads");
        }
        if (uploadDir == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload dir not configured");
            return;
        }
        Path file = Path.of(uploadDir, filename).normalize();
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String mime = Files.probeContentType(file);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        resp.setContentLengthLong(Files.size(file));
        try (OutputStream os = resp.getOutputStream()) {
            Files.copy(file, os);
        }
    }
}


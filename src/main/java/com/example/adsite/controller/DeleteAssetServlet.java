package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.dao.AdAssetDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles deletion of ad assets by the owning account.
 */
public class DeleteAssetServlet extends HttpServlet {
    private AdAssetDao adAssetDao;

    @Override
    public void init() throws ServletException {
        adAssetDao = ServiceRegistry.adAssetDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String idParam = req.getParameter("assetId");
        if (idParam != null && !idParam.isBlank()) {
            try {
                long assetId = Long.parseLong(idParam);
                adAssetDao.findById(assetId, accountName).ifPresent(asset -> {
                    // Delete physical file if present
                    String uploadDir = getServletContext().getInitParameter("upload.dir");
                    if (uploadDir == null || uploadDir.isBlank()) {
                        uploadDir = getServletContext().getRealPath("/uploads");
                    }
                    if (uploadDir != null && !uploadDir.isBlank()) {
                        Path filePath = Path.of(uploadDir, asset.getFileName());
                        try {
                            Files.deleteIfExists(filePath);
                        } catch (IOException ignored) {
                            // ignore failure to delete file, still remove DB record
                        }
                    }
                    adAssetDao.delete(assetId, accountName);
                });
            } catch (NumberFormatException ignored) {
                // ignore invalid id
            }
        }
        resp.sendRedirect(req.getContextPath() + "/console/assets");
    }
}

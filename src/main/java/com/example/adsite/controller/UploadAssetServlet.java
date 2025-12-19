package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.dao.UserTagDao;
import com.example.adsite.model.AdAsset;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Handles upload of image/video assets by logged-in ad accounts.
 */
@MultipartConfig
public class UploadAssetServlet extends HttpServlet {
    private AdAssetDao adAssetDao;
    private UserTagDao userTagDao;

    @Override
    public void init() throws ServletException {
        adAssetDao = ServiceRegistry.adAssetDao();
        userTagDao = ServiceRegistry.userTagDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        req.setAttribute("tags", userTagDao.supportedTags());
        req.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String tag = req.getParameter("tag");
        if (tag == null || !userTagDao.supportedTags().contains(tag)) {
            req.setAttribute("error", "请选择有效的标签");
            doGet(req, resp);
            return;
        }

        Part filePart = req.getPart("asset");
        if (filePart == null || filePart.getSize() == 0) {
            req.setAttribute("error", "请上传文件");
            doGet(req, resp);
            return;
        }

        String uuidName = UUID.randomUUID().toString();
        String submittedName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int dot = submittedName.lastIndexOf('.');
        if (dot > -1) {
            ext = submittedName.substring(dot);
        }
        String storedName = uuidName + ext;

        // Upload path is configured as Tomcat virtual path /uploads mapped to a disk directory.
        String uploadDir = getServletContext().getInitParameter("upload.dir");
        if (uploadDir == null || uploadDir.isBlank()) {
            uploadDir = getServletContext().getRealPath("/uploads");
        }
        if (uploadDir == null) {
            throw new ServletException("Uploads path not configured");
        }
        Files.createDirectories(Path.of(uploadDir));
        Path target = Path.of(uploadDir, storedName);
        filePart.write(target.toString());

        String mime = filePart.getContentType();
        AdAsset.Format format = mime != null && mime.startsWith("video") ? AdAsset.Format.VIDEO : AdAsset.Format.IMAGE;

        AdAsset asset = new AdAsset(
                0,
                accountName,
                storedName,
                mime != null ? mime : "application/octet-stream",
                tag
        );
        adAssetDao.insert(asset);

        resp.sendRedirect(req.getContextPath() + "/console/assets");
    }
}

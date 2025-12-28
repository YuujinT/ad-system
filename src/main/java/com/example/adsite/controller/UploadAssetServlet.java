package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.dao.UserTagDao;
import com.example.adsite.model.AdAsset;
import com.example.adsite.service.AssetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * Handles upload of image/video assets by logged-in ad accounts.
 */
@MultipartConfig
public class UploadAssetServlet extends HttpServlet {
    private AssetService assetService;
    private UserTagDao userTagDao;

    @Override
    public void init() throws ServletException {
        assetService = ServiceRegistry.assetService();
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

        String storedName = assetService.generateStoredName(filePart.getSubmittedFileName());
        String uploadDir = assetService.resolveUploadDir(getServletContext());
        assetService.savePartToDisk(filePart, uploadDir, storedName);

        String mime = filePart.getContentType();
        AdAsset asset = new AdAsset(
                0,
                accountName,
                storedName,
                mime != null ? mime : "application/octet-stream",
                tag
        );
        assetService.insertAsset(asset);

        resp.sendRedirect(req.getContextPath() + "/console/assets");
    }
}

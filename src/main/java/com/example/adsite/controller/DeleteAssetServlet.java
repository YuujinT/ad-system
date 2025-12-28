package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.service.AssetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles deletion of ad assets by the owning account.
 */
public class DeleteAssetServlet extends HttpServlet {
    private AssetService assetService;

    @Override
    public void init() throws ServletException {
        assetService = ServiceRegistry.assetService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String idParam = req.getParameter("assetId");
        assetService.deleteAsset(idParam, accountName, getServletContext());
        resp.sendRedirect(req.getContextPath() + "/console/assets");
    }
}

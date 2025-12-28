package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.service.AssetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdAssetServlet extends HttpServlet {
    private AssetService assetService;

    @Override
    public void init() throws ServletException {
        assetService = ServiceRegistry.assetService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        req.setAttribute("assets", assetService.findByOwner(accountName));
        req.setAttribute("accountName", accountName);
        req.getRequestDispatcher("/WEB-INF/views/assets.jsp").forward(req, resp);
    }
}

package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.dao.AdAssetDao;
import com.example.adsite.model.AdAsset;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdAssetServlet extends HttpServlet {
    private AdAssetDao adAssetDao;

    @Override
    public void init() throws ServletException {
        adAssetDao = ServiceRegistry.adAssetDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accountName = (String) req.getSession().getAttribute("accountName");
        if (accountName == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        List<AdAsset> assets = adAssetDao.findByOwner(accountName);
        req.setAttribute("assets", assets);
        req.setAttribute("accountName", accountName);
        req.getRequestDispatcher("/WEB-INF/views/assets.jsp").forward(req, resp);
    }
}

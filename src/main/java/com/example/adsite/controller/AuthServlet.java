package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.dao.AdOwnerDao;
import com.example.adsite.model.AdOwner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

public class AuthServlet extends HttpServlet {
    private AdOwnerDao ownerDao;

    @Override
    public void init() throws ServletException {
        ownerDao = ServiceRegistry.ownerDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username == null || password == null) {
            resp.sendRedirect("login.jsp?error=invalid");
            return;
        }
        Optional<AdOwner> ownerOpt = ownerDao.findByUsername(username);
        if (ownerOpt.isEmpty() || !password.equals(ownerOpt.get().getPassword())) {
            resp.sendRedirect(req.getContextPath() + "/error-invalid-login.jsp");
            return;
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("accountName", ownerOpt.get().getAccountName());
        resp.sendRedirect(req.getContextPath() + "/console/assets");
    }
}

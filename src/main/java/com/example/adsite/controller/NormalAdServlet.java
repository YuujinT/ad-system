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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NormalAdServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private AdService adService;

    @Override
    public void init() throws ServletException {
        adService = ServiceRegistry.adService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        if (userId == null || userId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing userId");
            return;
        }
        Optional<AdAsset> adOpt = adService.pickAdForUser(userId, AdAsset.Format.IMAGE);
        if (adOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no ad available");
            return;
        }
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            AdAsset ad = adOpt.get();
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", ad.getId());
            payload.put("fileName", ad.getFileName());
            payload.put("contentType", ad.getContentType());
            payload.put("interestTag", ad.getInterestTag());
            writer.write(mapper.writeValueAsString(payload));
        }
    }
}

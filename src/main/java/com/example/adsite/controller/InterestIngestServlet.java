package com.example.adsite.controller;

import com.example.adsite.config.ServiceRegistry;
import com.example.adsite.service.InterestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class InterestIngestServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private InterestService interestService;

    @Override
    public void init() throws ServletException {
        interestService = ServiceRegistry.interestService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = null;
        String singleTag = null;
        try {
            JsonNode root = mapper.readTree(req.getInputStream());
            if (root != null && !root.isMissingNode()) {
                userId = root.path("id").asText(null);
                JsonNode tagNode = root.get("tag");
                if (tagNode != null && tagNode.isTextual()) {
                    singleTag = tagNode.asText();
                }
            }
        } catch (IOException ignored) {
            // ignore body parse errors and fall back to query params
        }
        if (userId == null || userId.isBlank()) {
            userId = req.getParameter("id");
        }
        if (singleTag == null || singleTag.isBlank()) {
            singleTag = req.getParameter("tag");
        }

        if (userId == null || userId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user id");
            return;
        }
        List<String> supported = ServiceRegistry.userTagDao().supportedTags();
        if (singleTag == null || singleTag.isBlank() || !supported.contains(singleTag)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid tag");
            return;
        }
        interestService.collectInterest(userId, Collections.singletonList(singleTag));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

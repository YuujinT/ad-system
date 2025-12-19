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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class InterestIngestServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private InterestService interestService;

    @Override
    public void init() throws ServletException {
        interestService = ServiceRegistry.interestService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> tags = new ArrayList<>();
        String userId;
        try (InputStream inputStream = req.getInputStream()) {
            JsonNode root = mapper.readTree(inputStream);
            userId = root.path("id").asText(null);
            JsonNode tagsNode = root.path("tags");
            if (tagsNode.isArray()) {
                Iterator<JsonNode> iterator = tagsNode.elements();
                while (iterator.hasNext()) {
                    JsonNode value = iterator.next();
                    if (value.isTextual()) {
                        tags.add(value.asText());
                    }
                }
            }
        }
        if (userId == null || userId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user id");
            return;
        }
        interestService.collectInterest(userId, tags);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

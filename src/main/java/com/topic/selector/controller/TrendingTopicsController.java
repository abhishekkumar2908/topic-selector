package com.topic.selector.controller;

import com.topic.selector.model.Story;
import com.topic.selector.services.TrendingTopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v0/trending")
public class TrendingTopicsController {

    private final TrendingTopicsService trendingTopicsService;

    @Autowired
    public TrendingTopicsController(TrendingTopicsService trendingTopicsService) {
        this.trendingTopicsService = trendingTopicsService;
    }

    @GetMapping
    public ResponseEntity<List<Story>> getTrendingTopics() {
        return ResponseEntity.ok(trendingTopicsService.fetchTrendingTopics());
    }
}

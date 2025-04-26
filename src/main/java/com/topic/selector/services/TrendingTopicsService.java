package com.topic.selector.services;

import com.topic.selector.model.Story;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrendingTopicsService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final AIContentService aiContentService;

    @Value("${hackernews.topstories.url}")
    private String topStoriesUrl;

    @Value("${hackernews.storydetail.url}")
    private String storyDetailUrl;

    public TrendingTopicsService(AIContentService aiContentService) {
        this.aiContentService = aiContentService;
    }

    public List<String> fetchTrendingTopics() {
        // Step 1: Fetch top story IDs
        Integer[] storyIds = restTemplate.getForObject(topStoriesUrl, Integer[].class);
        if (storyIds == null || storyIds.length == 0) {
            return List.of();
        }

        // Step 2: Fetch details for the first 10 stories
        List<String> linkedInPosts = new ArrayList<>();
        for (int i = 0; i < Math.min(1, storyIds.length); i++) {
            Story story = restTemplate.getForObject(storyDetailUrl, Story.class, storyIds[i]);
            if (story != null) {
                String linkedInPost = aiContentService.generateLinkedInPostFromUrl(story.getUrl());
                linkedInPosts.add(linkedInPost);
            }
        }

        return linkedInPosts;
    }






}

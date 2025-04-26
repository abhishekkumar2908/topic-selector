package com.topic.selector.controller;

import com.topic.selector.services.AIContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/ai")
public class AIContentController {

    private final AIContentService aiContentService;

    @Autowired
    public AIContentController(AIContentService aiContentService) {
        this.aiContentService = aiContentService;
    }

    @PostMapping("/linkedin-post")
    public ResponseEntity<String> generateLinkedInPost(@RequestParam String url) {
        try {
            String linkedInPost = aiContentService.generateLinkedInPostFromUrl(url);
            return ResponseEntity.ok(linkedInPost);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating LinkedIn post: " + e.getMessage());
        }
    }
}
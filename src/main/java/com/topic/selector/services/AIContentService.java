package com.topic.selector.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class AIContentService {

    private final ChatClient chatClient;

    @Autowired
    public AIContentService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateLinkedInPostFromUrl(String url) {
        try {
            // Step 1: Fetch article content
            String articleContent = fetchPageContent(url);

            if (articleContent.isBlank()) {
                return "Could not extract meaningful content from the page.";
            }

            // Step 2: Build a powerful prompt
            String prompt = buildLinkedInPrompt(articleContent, url);

            // Step 3: Ask AI to generate LinkedIn post
            return chatClient
                    .prompt(prompt)
                    .call()
                    .content();
        } catch (IOException e) {
            return "Failed to fetch URL content: " + e.getMessage();
        } catch (Exception e) {
            return "AI service error: " + e.getMessage();
        }
    }

    private String fetchPageContent(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                .timeout(7000)
                .get();

        String title = doc.title();

        Elements paragraphs = doc.select("article p, main p, div p"); // Focus on main sections
        String content = paragraphs.stream()
                .map(org.jsoup.nodes.Element::text)
                .filter(text -> text.length() > 50) // skip very short texts
                .limit(20) // avoid pulling entire book
                .collect(Collectors.joining("\n\n"));

        if (content.length() > 3500) {
            content = content.substring(0, 3500);
        }

        return "Title: " + title + "\n\n" + content;
    }

    private String buildLinkedInPrompt(String articleContent, String url) {
        return """
                Act as a professional LinkedIn content creator.
                Summarize the following article into a short LinkedIn post (under 200 words).

                Instructions:
                - Make it engaging and professional.
                - Mention the main idea clearly.
                - Add 2-3 relevant hashtags at the end.
                - Do not copy-paste the article directly.
                - Encourage conversation in the comments.

                Article to summarize:
                
                %s
                
                (Source: %s)
                """.formatted(articleContent, url);
    }
}
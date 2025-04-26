package com.topic.selector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;



@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

    private String title;
    private String url;
    private String by;

    public String getUrl() {
        return url;
    }
}

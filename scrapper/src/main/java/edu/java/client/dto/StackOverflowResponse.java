package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(List<StackOverflowItem> items) {
    public record StackOverflowItem(@JsonProperty("question_id") Long id,
                                    @JsonProperty("title") String title,
                                    @JsonProperty("link") String link,
                                    @JsonProperty("creation_date") OffsetDateTime createdAt,
                                    @JsonProperty("last_activity_date") OffsetDateTime updatedAt) {
    }
}

package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;


public record GitHubResponse(
        @NotNull @Min(0) Long id,
        @NotBlank String name,
        @JsonProperty("full_name") @NotBlank String fullName,
        @JsonProperty("created_at") @NotBlank OffsetDateTime createdAt,
        @JsonProperty("pushed_at") @NotNull OffsetDateTime pushedAt,
        @JsonProperty("updated_at") @NotNull OffsetDateTime updatedAt
) {

}

package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ListLinksResponse(
    @NotNull @Valid @JsonProperty("links") LinkResponse[] linkResponses,
    @Min(0) @NotNull @JsonProperty("size") Long size
) {

}

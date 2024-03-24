package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.hibernate.validator.constraints.URL;

public record LinkResponse(
    @NotNull @Min(0) @JsonProperty("id") Long id,
    @NotBlank @URL @JsonProperty("uri") URI uri
) {
}

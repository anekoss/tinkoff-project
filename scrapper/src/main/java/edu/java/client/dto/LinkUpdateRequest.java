package edu.java.client.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record LinkUpdateRequest(
    @Min(0) Long id,
    @NotBlank @URL String url,
    @NotBlank String description,
    @NotNull @NotEmpty long[] tgChatIds
) {

}

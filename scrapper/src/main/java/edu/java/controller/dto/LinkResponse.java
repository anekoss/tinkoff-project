package edu.java.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.hibernate.validator.constraints.URL;

public record LinkResponse(
    @NotNull @Min(0) Long id,
    @NotBlank @URL URI url
) {
}

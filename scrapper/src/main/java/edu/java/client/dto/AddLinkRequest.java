package edu.java.client.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(@NotBlank @URL String link) {
}

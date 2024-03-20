package edu.java.client;

import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.exception.BadResponseBodyException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static edu.java.client.ClientStatusCodeHandler.ERROR_RESPONSE_FILTER;


@Slf4j
@Component
public class BotClient {

    private final WebClient webCLient;

    public BotClient(
            @Value("${app.client.botClient.base-url}")
            @NotBlank @URL String url) {
        this.webCLient = WebClient.builder().filter(ERROR_RESPONSE_FILTER).baseUrl(url).build();
    }

    public String linkUpdates(LinkUpdateRequest request) throws BadResponseBodyException {
        try {
            return webCLient
                    .post()
                    .uri("/updates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), LinkUpdateRequest.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }
}

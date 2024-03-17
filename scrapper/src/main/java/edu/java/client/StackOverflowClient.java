package edu.java.client;

import edu.java.client.dto.StackOverflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class StackOverflowClient {
    private final String defaultUrl = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClient(String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public StackOverflowClient() {
        this.webClient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public StackOverflowResponse fetchQuestion(Integer id) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("2.3/questions/{id}").queryParam("site", "stackoverflow")
                .queryParam("sort", "activity").build(id))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .onErrorMap(error -> {
                log.error(error.getMessage());
                throw new IllegalArgumentException("No response body was returned from the service");
            })
            .block();
    }

}
